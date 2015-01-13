package org.ntip.androidApp.guessReel.client.ui;



import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import org.ntip.androidApp.guessReel.client.GameUtility;
import org.ntip.androidApp.guessReel.client.model.QuestionSet;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.PendingCall;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainFragment extends Fragment {

	private static final String TAG="MainFragment";
	
	@InjectView(R.id.userName)
	protected EditText userName_;

	@InjectView(R.id.password)
	protected EditText password_;

	@InjectView(R.id.server)
	protected EditText server_;
	
	Menu menu;
	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	
	Button updateFB_btn;
	
	private Session.StatusCallback sessionCallback = new Session.StatusCallback() {//variable that contains the implementation for listener
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			// TODO Auto-generated method stub
			onSessionStateChange(session,state,exception);
		}
	};
	
	private UiLifecycleHelper uiHelper;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(),sessionCallback);
		uiHelper.onCreate(savedInstanceState);//creates FB session and opens it automatically if a cached token is available
		
		ButterKnife.inject(this,getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	   // View view = inflater.inflate(R.layout.activity_main, container, false);
		View view = inflater.inflate(R.layout.activity_login_screen, container, false);
	    
	    
	    LoginButton authButton = (LoginButton)view.findViewById(R.id.authButton);
	    authButton.setFragment(this);//set this fragment to receive onActivityResult()
	   // authButton.setReadPermissions(Arrays.asList("user_likes","user_status"));
	   
	    updateFB_btn = (Button)view.findViewById(R.id.display);
	    updateFB_btn.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	           // publishStory();  
	        	 FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(getActivity())
	             .setLink(null)
	             .build();
	     	    uiHelper.trackPendingDialogCall(shareDialog.present());
	        }
	    });
	    
	    FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(getActivity())
        .setLink(null)
        .build();
	    uiHelper.trackPendingDialogCall(shareDialog.present());

	    
	    if (savedInstanceState != null) {
	        pendingPublishReauthorization = 
	            savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
	    }
	    return view;
	}
	
	@OnClick(R.id.loginButton)
	public void login() {
		
		final Collection<QuestionSet> qsData;
		String user = userName_.getText().toString();
		String pass = password_.getText().toString();
		String server = server_.getText().toString();
		
		
		if(user.equalsIgnoreCase("") || pass.equalsIgnoreCase("")){
			Toast.makeText(getActivity(), "Oops!! Please enter your credentials", Toast.LENGTH_SHORT).show();
			//startActivity(new Intent(this,LoginScreenActivity.class));	
			//this.recreate();
		}else{
		

		String credentials = user + "," + pass + "," + server;
		
		if(!GameUtility.addUserPreferences(getActivity(),credentials))
			Log.d(TAG,"Unable to store user credentials");
		else{
			Log.d(TAG,"TESTING:User credentials stored in" + this.getActivity());
			String[] userData = GameUtility.getUserPreferences(getActivity());
			Log.d(TAG,"User::" + userData[0] + "pass::" + userData[1] + "server::" + userData[2]);
		}
		
		Intent i = new Intent(getActivity(),
				LaunchActivity.class);
		startActivity(i);
		}	
		
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception){
		if(state.isOpened()){
			Log.i(TAG,"logged in");
			updateFB_btn.setVisibility(View.VISIBLE);
			
			//Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, Arrays.asList("user_checkins"));
			//session.requestNewReadPermissions(newPermissionsRequest);
			
			if (pendingPublishReauthorization && 
			        state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
			    pendingPublishReauthorization = false;
			    publishStory();
			}
			
		}else if(state.isClosed()){
			Log.i(TAG,"Logged out");
			updateFB_btn.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		 uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback(){

			@Override
			public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
				// TODO Auto-generated method stub
				Log.i("Fragment/Activity", "success");
				
			}

			@Override
			public void onError(FacebookDialog.PendingCall pendingCall, Exception error,
					Bundle data) {
				// TODO Auto-generated method stub
				Log.i("Fragment/Activity",String.format("Error: %s", error.toString()));
			}
			 
		 });
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();//For scenarios where 
		if(session != null && session.isOpened() || session.isClosed()){
			onSessionStateChange(session,session.getState(),null);
		}
		uiHelper.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		 outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
		uiHelper.onSaveInstanceState(outState);
	}
	
	private void publishStory() {
	    Session session = Session.getActiveSession();

	    if (session != null){

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	            return;
	        }

	        Bundle postParams = new Bundle();
	        postParams.putString("name", "Facebook SDK for Android");
	        postParams.putString("caption", "Build great social apps and get more installs.");
	        postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	        postParams.putString("link", "https://developers.facebook.com/android");
	        postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

	        Request.Callback callback= new Request.Callback() {
	            @Override
	        	public void onCompleted(Response response) {
	                JSONObject graphResponse = response
	                                           .getGraphObject()
	                                           .getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                    Log.i(TAG,
	                        "JSON error "+ e.getMessage());
	                }
	                FacebookRequestError error = response.getError();
	                if (error != null) {
	                    Toast.makeText(getActivity()
	                         .getApplicationContext(),
	                         error.getErrorMessage(),
	                         Toast.LENGTH_SHORT).show();
	                    } else {
	                        Toast.makeText(getActivity()
	                             .getApplicationContext(), 
	                             postId,
	                             Toast.LENGTH_LONG).show();
	                }
	            }
	        };

	        Request request = new Request(session, "me/feed", postParams, 
	                              HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    }

	}
	
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
}
