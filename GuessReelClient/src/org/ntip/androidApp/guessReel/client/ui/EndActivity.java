package org.ntip.androidApp.guessReel.client.ui;


import org.ntip.androidApp.guessReel.client.AudioPlayer;
import org.ntip.androidApp.guessReel.client.GameUtility;
import org.ntip.androidApp.guessReel.client.GameUtility.status;
import org.ntip.androidApp.guessReel.client.service.GameIntentService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EndActivity extends Activity {
	public static final String TAG = "EndActivity";

	public boolean Game_Status;
	EditText game_status;
	int userPoints;
	
	private AudioPlayer mPlayer = new AudioPlayer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.end_activity);
		
		game_status =(EditText)findViewById(R.id.GameStatus);
		
		Button play_again = (Button)findViewById(R.id.PlayAgain);
		play_again.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				Log.d(TAG,"Launch activity launched");
				startActivity(new Intent(getApplicationContext(),LaunchActivity.class));
				
			}
			});
			
		userPoints = GameUtility.getPointsPreferences(this);
		
		Game_Status = getIntent().getExtras().getBoolean("GameStatus");
		Log.d(TAG,"Game status" + Game_Status);
		
		if(Game_Status){//check for GAme_Lost
			mPlayer.play(this, "loose");
			Log.d(TAG,"loose audio played");
			game_status.setText("Sorry U Lost! U earned::::" + userPoints + " so far.Great!!");
			//MediaPlayer.create(this,R.raw.win);
			
		}
		else{
			Toast.makeText(this, "Yay!U Won the Game", Toast.LENGTH_SHORT).show();
			mPlayer.play(this,"win");
			Log.d(TAG,"win audio played");
			game_status.setText("Wow.U WON!!U earned::::" + userPoints + " so far.Great!!");
			
		}
		String updatedRatings = GameUtility.getRatings(this);
    	Log.d(TAG,"updated rating before adding to Intent" + updatedRatings);
    	Intent i = new Intent(EndActivity.this,GameIntentService.class);
    	i.putExtra("updatedRatings",GameUtility.getRatings(this));
    	startService(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//onPauseGameSoundConfig(mSoundPool);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//onResumeSoundConfig();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mPlayer.stop();
	}
	

}
