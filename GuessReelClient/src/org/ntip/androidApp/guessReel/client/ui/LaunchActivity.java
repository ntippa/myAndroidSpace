package org.ntip.androidApp.guessReel.client.ui;

import java.util.ArrayList;


import java.util.Collection;
import java.util.concurrent.Callable;


import org.ntip.androidApp.guessReel.client.CallableTask;
import org.ntip.androidApp.guessReel.client.GameSvc;
import org.ntip.androidApp.guessReel.client.GameSvcApi;
import org.ntip.androidApp.guessReel.client.GameUtility;
import org.ntip.androidApp.guessReel.client.TaskCallback;
import org.ntip.androidApp.guessReel.client.model.Points;
import org.ntip.androidApp.guessReel.client.model.QuestionSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LaunchActivity extends Activity {
	static final String TAG = "LaunchActivity.class";
	String[] userData;
	ArrayList<QuestionSet> mQSets;
	Points userPointsData;
	int userPoints;

	/* Obtain user credentials from user preferences
	 * with button click, fetch questionSets, userPoints over http
	 * on success, launch MainActivity
	*
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_activity);
		
		
		
		Log.d(TAG, "Before clearing shPref's");
		 GameUtility.clearSharedPreference(getApplicationContext());
		 Log.d(TAG,"Cleared USER_CORRECT_ATTEMPTS:getCorrectAttempts:: " + GameUtility.getCorrectAttempts(getApplicationContext()));
	    // GameUtility.clearSharedPreference(getApplicationContext(), GameUtility.USER_INCORRECT_ATTEMPTS);
	     Log.d(TAG,"Cleared USER_INCORRECT_ATTEMPTS:getIncorrectAttempts:: " + GameUtility.getIncorrectAttempts(getApplicationContext()));
		
		userData = GameUtility.getUserPreferences(this);
		Log.d(TAG,"User::" + userData[0] + "pass::" + userData[1] + "server::" + userData[2]);
		
		Button launch_button = (Button)findViewById(R.id.launch);
		launch_button.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				
				final GameSvcApi svc = GameSvc.init(userData[2] , userData[0], userData[1]);
				CallableTask.invoke(new Callable<Collection<QuestionSet>>() {				

					@Override
					public Collection<QuestionSet> call() throws Exception {
						//qsData = svc.loadGame();
						return svc.loadGame();									// fetch question sets
					}
				}, new TaskCallback<Collection<QuestionSet>>() {

					@Override
					public void success(Collection<QuestionSet> result) {		//on success
						
						CallableTask.invoke(new Callable<Points>() {

							@Override
							public Points call() throws Exception {
								Log.d(TAG,"before getUserPoints");
								return svc.getUserPoints();										// fetch user points
							}
						}, new TaskCallback<Points>() {

							@Override
							public void success(Points _userPoints) {
								Log.d(TAG,"success loadGame");
								userPointsData = _userPoints;
								Log.d(TAG,"userpoints" + _userPoints.getPoints());
								userPoints = userPointsData.getPoints();
								Log.d(TAG,"userPoints" + userPoints);
								
								//add to shared preferences
								//if(!GameUtility.addPointsPreferences(LaunchActivity.this,userPoints))
								if(!GameUtility.addPointsPreferences(getApplicationContext(),userPoints))
									Log.d(TAG,"Unable to store user points to preferences");
								/*else{
									int points = GameUtility.getPointsPreferences(getApplicationContext());
									Log.d(TAG,"userPoints Preferences::" + points);
								}*/
								
							}

							@Override
							public void error(Exception e) {
								Log.e(LaunchActivity.class.getName(), "Error invoking getUserPoints data", e);
								
								Toast.makeText(
										LaunchActivity.this,
										"Failed to fetch User points",
										Toast.LENGTH_SHORT).show();
							}
						});
					
							// At this point, we have sucess fetching questionSets, user points
							mQSets = (ArrayList<QuestionSet>) result;
							if(mQSets == null){
					        	Log.d(TAG,"mQSets is not null");
					        }else{
					        	Log.d(TAG,mQSets.get(0).getQuestions().get(0).toString());
					        	Log.d(TAG,mQSets.get(0).getExplanation().toString());
					        }
							
							
							Intent i = new Intent(LaunchActivity.this,				// Intent Main activity
									MainActivity.class);
							i.putExtra("QuestionSets",mQSets);
							i.putExtra("UserPoints",userPoints);
							startActivity(i);
						
					}

					@Override
					public void error(Exception e) {
						Log.e(LaunchActivity.class.getName(), "Error fetching question sets.", e);
						
						Toast.makeText(
								LaunchActivity.this,
								"Failed to launch Game",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			
		});
	
		Button rules = (Button)findViewById(R.id.rules);
		rules.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				String text = "Each screen presents a movie puzzle."
						+ "It displays 4 movie names. User needs to pick the odd movie."
						+ "10 puzzles make a game. User looses the game with 3 incorrect guesses";
				TextView game_rules = (TextView)findViewById(R.id.game_rules);
				game_rules.setText(text);
				
			}
			});
		
	}//onCreate

	
}// LaunchActivity
