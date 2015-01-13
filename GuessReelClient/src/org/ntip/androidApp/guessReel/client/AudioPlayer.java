package org.ntip.androidApp.guessReel.client;



import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {
	
	private MediaPlayer mPlayer;
	
	public void stop(){
		if(mPlayer != null){
			mPlayer.release();
			mPlayer = null;
		}
	}

	public void play(Context c, String audioFile){
		stop();
		if(audioFile.equals("correct")){
			mPlayer = MediaPlayer.create(c,R.raw.applause);
		}else if(audioFile.equals("incorrect")){
			mPlayer = MediaPlayer.create(c,R.raw.incorrect);
		}else if(audioFile.equals("win")){
			mPlayer = MediaPlayer.create(c,R.raw.win);
		}else if(audioFile.equals("loose")){
			mPlayer = MediaPlayer.create(c,R.raw.loose);
		}
		
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				stop();
			}
		});
		
		mPlayer.start();
	}
}
