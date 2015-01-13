package org.ntip.androidApp.guessReel.client.ui;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import org.ntip.androidApp.guessReel.client.AudioPlayer;
import org.ntip.androidApp.guessReel.client.GameUtility;
import org.ntip.androidApp.guessReel.client.model.QuestionSet;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;




public class QuestionSetListFragment extends ListFragment {
	static final String TAG = "QuestionSetListFragment.class";
   String mNum, Movie_id;
   int userPoints;
   int qs_position; // if last item
  float rating = 0.5f;
  String explanation;
   
    
    TextView titleView, corr_ans_View;
    RatingBar ratingBar;
    
    Menu menu;
    MenuItem pointsItem;
    String pointsStatus = "Points";
    
   Collection<QuestionSet> qSets;
   HashMap<String,Float> updatedRatings = new HashMap<String,Float>();
   
    boolean alreadyClicked; // check if a choice was already made
    
    private AudioPlayer mPlayer = new AudioPlayer();
    

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static QuestionSetListFragment newInstance(QuestionSet qs, int position) {
        QuestionSetListFragment f = new QuestionSetListFragment();
        String[] questions = qs.getQuestions().toArray(new String[]{});
        String id = qs.getId();
        String correct = qs.getCorrectAnswer();
        String explanation = qs.getExplanation();
       // Log.i("QuestionSetListFragemnt:newInstance:num" , String.valueOf(num));
        // Supply num input as an argument.
        Log.d(TAG,"QS:id"+id);
        Log.d(TAG,"QS:correct"+correct);
        Log.d(TAG,"QS:questions"+questions[0]+questions[1]);
        Log.d(TAG,"Position" + position);
       
        Bundle args = new Bundle();
        args.putStringArray("questions", questions);
        args.putString("id", id);
        args.putString("correct", correct);
        args.putString("explanation", explanation);
       args.putInt("position", position);
       args.putFloat("rating", qs.getRating());
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
        setRetainInstance(true);   // retain instance upon config changes
        //mNum is position of Fragment
     
  		 
       Movie_id = getArguments() != null ? getArguments().getString("id") : String.valueOf(1);
      // userPoints = getArguments() != null ? getArguments().getInt("userPoints") : 0;//default points 0
       qs_position = getArguments().getInt("position");
       rating = getArguments().getFloat("rating");
       explanation = getArguments().getString(explanation);
       userPoints = GameUtility.getPointsPreferences(getActivity());
       Log.d(TAG,"UserPoints:onCReate" + userPoints);
      /* 
        Log.d("QuestionSetListFragemnt:onCreate::ID" , String.valueOf(mNum));
        */
        alreadyClicked = false;
        
        Log.d(TAG,"GameUtilitiy.NUM_QUESTIONS" + GameUtility.NUM_QUESTIONS);
        
         
    }

    
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    
    	Log.d(TAG,"onCreateView");
        View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
        
       
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.HONEYCOMB){
        	if(NavUtils.getParentActivityName(getActivity())!= null){
        	getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        	}
        }
       
        int puzzleNum = qs_position + 1;
         titleView =(TextView) v.findViewById(R.id.text);
         titleView.setText("Movie Puzzle: " + puzzleNum + "/" + (GameUtility.NUM_QUESTIONS));//displays the puzzle number
        // corr_ans_View=(TextView)v.findViewById(R.id.correct);
        
         ratingBar = (RatingBar)v.findViewById(R.id.rating);
         ratingBar.setRating(rating);
         ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
     		public void onRatingChanged(RatingBar ratingBar, float rating,
     			boolean fromUser) {
      
     			Log.d(TAG,"rating changed");
     			String temp = GameUtility.getRatings(getActivity());
     			temp = temp  + Movie_id + "," + String.valueOf(rating) + ",";
     			Log.d(TAG,"ratng string b4 adding to shared pref" + temp);
     			GameUtility.addRatings(getActivity(), temp);
     			
     			Log.d(TAG,"updated rating string from Prefs" + GameUtility.getRatings(getActivity()));
     			//titleView.setText(String.valueOf(rating));
      
     		}
     	});
       
       
      // titleView.setText("Movie Puzzle" + Movie_id);
        // titleView.setText("Points" + userPoints );
        
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	//ArrayList<QuestionSet> mQSets = QuestionLab.get(getActivity()).getQuestionSets();
        super.onActivityCreated(savedInstanceState);
        
        String[] questions = getArguments() != null ? getArguments().getStringArray("questions") : new String[]{};
        Log.d("onActivityCreated::questions:",questions.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        										android.R.layout.simple_list_item_1,questions);
        setListAdapter(adapter);
        
        
        
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	int correct_attempts;
    	int incorrect_attempts;
    	String correct = getArguments().getString("correct");
    	String explanation = getArguments().getString("explanation");
    	String qs = (String)(getListAdapter()).getItem(position);
    	
    	if(alreadyClicked){																	// already clicked an option
    		Toast.makeText(getActivity(), "Oops!! U already made a selection!!", Toast.LENGTH_SHORT).show();
    		
    		
    	}else{//first time making a selection
    		alreadyClicked = true;
    		if(qs.equals(correct)){		//CORRECT GUESS
    			Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
    			mPlayer.play(getActivity(),"correct");
    			
    			userPoints = GameUtility.getPointsPreferences(getActivity());
    			Log.d(TAG,"user points from Prefs" + userPoints);
	    		userPoints = userPoints + 1;					
	    		//add to shared preferences
				if(!GameUtility.addPointsPreferences(getActivity(),userPoints))		// updating user points
					Log.d(TAG,"Unable to store user points to preferences");
				
	    	}else{//INCORRECT GUESS
	    		mPlayer.play(getActivity(), "incorrect");
	    		
	    		titleView.setText(explanation);
	    		
    			Toast.makeText(getActivity(), "Oops!Incorrect!!", Toast.LENGTH_SHORT).show();
    			incorrect_attempts = GameUtility.getIncorrectAttempts(getActivity()); // increment incorrect attempts
    			incorrect_attempts = incorrect_attempts + 1;
    			if(incorrect_attempts == GameUtility.ATTEMPTS_TO_LOOSE){//3 attempts done
    				GameUtility.GAME_LOST = true;
    				Log.d(TAG,"incorrect_attempts shuld be 3" + incorrect_attempts);
    				Log.d(TAG,"Oops!U Lost the Game");
    				
    			}else{//incorrect_attempts < 3
    				Toast.makeText(getActivity(), "Oops!U have" + String.valueOf(GameUtility.ATTEMPTS_TO_LOOSE - incorrect_attempts) + " chances left", Toast.LENGTH_SHORT).show();
    				if(!GameUtility.addIncorrectAttempts(getActivity(),incorrect_attempts))
    					Log.d(TAG,"Unable to store incorrect attempts");
    			}//incorrect_attempts < 3
    		}
    	}
    	
    	userPoints = GameUtility.getPointsPreferences(getActivity());
    	
    	pointsStatus = String.valueOf(userPoints);
    	pointsStatus = "Ur Points::" + pointsStatus;
    	//menu.add(pointsStatus);
    	menu.getItem(0).setTitle(pointsStatus);
    		
    	if(qs_position ==(GameUtility.NUM_QUESTIONS -1)){// if last item of list .Maybe there is a better way to do it??
        	Log.d(TAG,"last item selected");
        	Toast.makeText(getActivity(), "End of Game", Toast.LENGTH_SHORT).show();//START INTENT SERVICE
        	Log.d(TAG,"Launching EndActivity");
        	Intent endActivityIntent = new Intent(getActivity(),EndActivity.class);
        	endActivityIntent.putExtra("GameStatus", GameUtility.GAME_LOST);					// start EndActivity
        	startActivity(endActivityIntent);
        }
        else
        	Log.d(TAG,"Not the last item");
    
    	//((TextView)titleView).setText("It is" + correct+ explanation);
    	}//onListItemClick
    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	Log.d(TAG,"onCreateOptionsMenu");
    	String[] credentials = GameUtility.getUserPreferences(getActivity());
    	if(credentials[0].equals("admin"))//only admin
    		inflater.inflate(R.menu.admin_menu, menu);
    	else
    		inflater.inflate(R.menu.activity_menu, menu);
		this.menu = menu;
    	//menu.add(pointsStatus);
	}
   
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(NavUtils.getParentActivityName(getActivity())!= null){
				NavUtils.navigateUpFromSameTask(getActivity());
			}
		case R.id.activity_menu_item:
			item.setTitle(pointsStatus);
			return true;
		case R.id.admin_menu_delete:
			Log.d(TAG,"opted for deletion");
			String temp = GameUtility.getQuestionsDelete(getActivity());
			temp = temp + Movie_id + "," ;
			GameUtility.addQuestionsDelete(getActivity(), temp);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
   
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d(TAG,"ListFragment onDestroyView called");
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mPlayer.stop();
		Log.d(TAG,"onDestroy called");
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.d(TAG,"onDetach called");
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
		Log.d(TAG,"onPause called");
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG,"onStop called");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
 
    
    
}//class



