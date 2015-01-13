package org.ntip.androidApp.guessReel.client.ui;

import java.util.Collection;
import java.util.concurrent.Callable;


import org.ntip.androidApp.guessReel.client.CallableTask;
import org.ntip.androidApp.guessReel.client.GameSvc;
import org.ntip.androidApp.guessReel.client.GameSvcApi;
import org.ntip.androidApp.guessReel.client.GameUtility;
import org.ntip.androidApp.guessReel.client.TaskCallback;
import org.ntip.androidApp.guessReel.client.model.QuestionSet;
import org.ntip.androidApp.guessReel.client.model.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {


	static final String TAG = " RegisterActivity.class";

	@InjectView(R.id.userName)
	protected EditText userName_;

	@InjectView(R.id.password)
	protected EditText password_;

	@InjectView(R.id.server)
	protected EditText server_;
	
	Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		ButterKnife.inject(this);
	}

	@OnClick(R.id.registerButton)
	public void login() {
		
		final Collection<QuestionSet> qsData;
		final String user = userName_.getText().toString();
		final String pass = password_.getText().toString();
		final String server = server_.getText().toString();
		String credentials = user + "," + pass + "," + server;
		final String newUser_credentials =  user + "," + pass; 
		
		if(!GameUtility.addUserPreferences(this,credentials))
			Log.d(TAG,"Unable to store user credentials");
		else{
			Log.d(TAG,"TESTING:User credentials stored in" + this.getLocalClassName());
			String[] userData = GameUtility.getUserPreferences(this);
			Log.d(TAG,"User::" + userData[0] + "pass::" + userData[1] + "server::" + userData[2]);
		}

		//final VideoSvcApi svc = VideoSvc.init(server, user, pass);
		//final GameSvcApi svc = GameSvc.init(server, user, pass);
		final GameSvcApi svc = GameSvc.init(server, "user", "pass");
		
		CallableTask.invoke(new Callable<User>() {

			@Override
			public User call() throws Exception {
				Log.d(TAG,"before getnewUser");
				return svc.getNewUser(user,pass);
			}
		}, new TaskCallback<User>() {

			@Override
			public void success(User result) {
				// OAuth 2.0 grant was successful and we
				// can talk to the server, open up the video listing
				startActivity(new Intent(
						RegisterActivity.this,
						LaunchActivity.class));
			}

			@Override
			public void error(Exception e) {
				Log.e(RegisterActivity.class.getName(), "Error registering.", e);
				
				Toast.makeText(
						RegisterActivity.this,
						"Registration failed, check your Internet connection and credentials.",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
		
		/*if(!GameUtility.addUserPreferences(this,credentials))
			Log.d(TAG,"Unable to store user credentials");
		else{
			Log.d(TAG,"TESTING:User credentials stored in" + this.getLocalClassName());
			String[] userData = GameUtility.getUserPreferences(this);
			Log.d(TAG,"User::" + userData[0] + "pass::" + userData[1] + "server::" + userData[2]);
		}
		
		Intent i = new Intent(LoginScreenActivity.this,
				LaunchActivity.class);
		startActivity(i);
		
		
	}*/
}
