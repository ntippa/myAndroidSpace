package org.ntip.androidApp.guessReel.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class GameUtility {
	public static final String TAG = "GameUtility";
	
	public static  int NUM_QUESTIONS;
	public static final int DEFAULT_POINTS = 1;
	public static final int DEFAULT_ATTEMPTS = 0;
	public static final int ATTEMPTS_TO_WIN = 3;//dnt need
	public static final int ATTEMPTS_TO_LOOSE = 3;
	
	public static  boolean GAME_LOST = false;

	public static  final String GAME_CREDENTIALS = "credentials";
	public static final String USER_POINTS = "points";
	public static final String USER_RATINGS = "ratings";
	public static final String USER_CORRECT_ATTEMPTS = "correct";
	public static final String USER_INCORRECT_ATTEMPTS = "incorrect";
	public static final String DELETE_QS = "delete_qs";
	
	public static int mSoundId_correct,mSoundId_incorrect,mSoundId_WIN,mSoundId_LOOSE;
	private static int mVolume = 6;
	private static  int mVolumeMax = 10;
	private static int mVolumeMin = 0;
	private static SoundPool mSoundPool;
	
	private  static AudioManager mAudioManager;
	private static boolean mCanPlayAudio;
	
	public enum status{CORRECT,INCORRECT,WIN,LOOSE}
	
   public static SoundPool getSoundPool(){
	   mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	   return mSoundPool;
   }
	
	public static void soundConfig(Context context,SoundPool sp){
		 SoundPool mSoundPool;
		// Get reference to the AudioManager
	    // mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		//mAudioManager = am;
	     	// Create a SoundPool
	  		//mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			mSoundPool = sp;
	  		// Load bubble popping sound into the SoundPool
	  		mSoundId_incorrect = mSoundPool.load(context, R.raw.incorrect, 1);
	  		mSoundId_correct = mSoundPool.load(context, R.raw.applause, 1);
	  		//mSoundId_WIN = mSoundPool.load(context, R.raw.win, 1);
	  		//mSoundId_LOOSE = mSoundPool.load(context, R.raw.loose, 1);

	  		// Set an OnLoadCompleteListener on the SoundPool
	  		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

	  			@Override
	  			public void onLoadComplete(SoundPool soundPool, int sampleId,
	  					int status) {

	  				// If sound loading was successful enable the play Button
	  				if (0 == status) {
	  					Log.d(TAG,"loaded sound");
	  				} else {
	  					Log.i(TAG, "Unable to load sound");
	  					//finish();//TODO::dnt know what to do for this
	  				}
	  			}
	  		});
	}
	
	public static boolean soundGameConfig(Context context,SoundPool sp){
		 
		// Get reference to the AudioManager
	    // mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
	     	// Create a SoundPool
	  		SoundPool mSoundPool;
	  		//mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	  		mSoundPool = sp;
	  		// Load bubble popping sound into the SoundPool
	  		//mSoundId_incorrect = mSoundPool.load(context, R.raw.slow_whoop_bubble_pop, 1);
	  		//mSoundId_correct = mSoundPool.load(context, R.raw.applause, 1);
	  		mSoundId_WIN = mSoundPool.load(context, R.raw.win, 1);
	  		mSoundId_LOOSE = mSoundPool.load(context, R.raw.loose, 1);

	  		// Set an OnLoadCompleteListener on the SoundPool
	  		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

	  			@Override
	  			public void onLoadComplete(SoundPool soundPool, int sampleId,
	  					int status) {

	  				// If sound loading was successful enable the play Button
	  				if (0 == status) {
	  					Log.d(TAG,"loaded sound");
	  				} else {
	  					Log.i(TAG, "Unable to load sound");
	  					//finish();//TODO::dnt know what to do for this
	  				}
	  			}
	  		});
	  		return true;
	}
	public static void onPauseSoundConfig(SoundPool mSoundPool){
		if (null != mSoundPool) {
			mSoundPool.unload(mSoundId_correct);
			mSoundPool.unload(mSoundId_incorrect);
			//mSoundPool.unload(mSoundId_WIN);
			//mSoundPool.unload(mSoundId_LOOSE);
			mSoundPool.release();
			mSoundPool = null;
		}

		mAudioManager.setSpeakerphoneOn(false);
		mAudioManager.unloadSoundEffects();
	}
	
	public static void onPauseGameSoundConfig(SoundPool mSoundPool){
		mSoundPool = getSoundPool();
		if (null != mSoundPool) {
			//mSoundPool.unload(mSoundId_correct);
			//mSoundPool.unload(mSoundId_incorrect);
			mSoundPool.unload(mSoundId_WIN);
			mSoundPool.unload(mSoundId_LOOSE);
			mSoundPool.release();
			mSoundPool = null;
		}

		mAudioManager.setSpeakerphoneOn(false);
		mAudioManager.unloadSoundEffects();
	}
	
	public static void onResumeSoundConfig(){
		mAudioManager.setSpeakerphoneOn(true);
		mAudioManager.loadSoundEffects();
	}
	
	public static void playSound(Context context,status gameStatus){
		mSoundPool = getSoundPool();
		
		switch(gameStatus){
		case CORRECT:
			mSoundPool.play(mSoundId_correct, (float) mVolume / mVolumeMax,
				(float) mVolume / mVolumeMax, 1, 0, 2.0f);
			break;
		case INCORRECT:
			mSoundPool.play(mSoundId_incorrect, (float) mVolume / mVolumeMax,
					(float) mVolume / mVolumeMax, 1, 0, 2.0f);
			break;
		case WIN:
			mSoundPool.play(mSoundId_WIN, (float) mVolume / mVolumeMax,
					(float) mVolume / mVolumeMax, 1, 0, 2.0f);
			break;
		case LOOSE:
			mSoundPool.play(mSoundId_LOOSE, (float) mVolume / mVolumeMax,
					(float) mVolume / mVolumeMax, 1, 0, 1.0f);
			break;
		default:
			break;
		}
	}
	//add questions for deletion
	public static boolean addQuestionsDelete(Context context,String markedDelete){
		return addPreferences(context,DELETE_QS,markedDelete);
	}
	
	//get questions marked for deletion
		public static String getQuestionsDelete(Context context){
			return getFromPreferences(context,DELETE_QS,",");
		}
	
	//add user ratings
	public static boolean addRatings(Context context,String ratings){
		return addPreferences(context,USER_RATINGS,ratings);
	}
	
	//get user ratings
	public static String getRatings(Context context){
		return getFromPreferences(context,USER_RATINGS,",");
	}
	
	//add correct attempts
	public static boolean addCorrectAttempts(Context context,int correctAttempts){
		String correct = String.valueOf(correctAttempts);
		return addPreferences(context,USER_CORRECT_ATTEMPTS,correct);
		
	}
	
	//get correct attempts
	public static int getCorrectAttempts(Context context){
		String tempCorrect = getFromPreferences(context,USER_CORRECT_ATTEMPTS,String.valueOf(DEFAULT_ATTEMPTS));//default=0
		return Integer.parseInt(tempCorrect);
		
	}
	
	//add incorrect attempts
	public static boolean addIncorrectAttempts(Context context, int incorrectAttempts){
		String incorrect = String.valueOf(incorrectAttempts);
		return addPreferences(context,USER_INCORRECT_ATTEMPTS,incorrect);
	}
	
	public static int getIncorrectAttempts(Context context){
		String tempIncorrect = getFromPreferences(context,USER_INCORRECT_ATTEMPTS,String.valueOf(DEFAULT_ATTEMPTS));
		return Integer.parseInt(tempIncorrect);
	}
	
	
	// add user points
	public static boolean addPointsPreferences(Context context,int points){
		String userpoints = String.valueOf(points);
		return addPreferences(context,USER_POINTS,userpoints);
	}
	
	//get user points
	public static int getPointsPreferences(Context context){
		String tempPoints = getFromPreferences(context,USER_POINTS,String.valueOf(DEFAULT_POINTS));
		return Integer.parseInt(tempPoints);
	}
	
	// add user credentials
	public static boolean addUserPreferences(Context context,String credentials){
		return addPreferences(context,GAME_CREDENTIALS,credentials);
	}
	
	//get user credentials
	public static String[] getUserPreferences(Context context){
		String temp = getFromPreferences(context,GAME_CREDENTIALS,null);
		return convertToArray(temp);
	}
	
	private static String getFromPreferences(Context context, String Key,String defValue){
		String Value = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(Key, defValue);
		return Value;
	}
	
	private static boolean addPreferences(Context context, String Key, String Value){
		PreferenceManager.getDefaultSharedPreferences(context)
		.edit()
		.putString(Key,Value)
		.commit();
		return true;
	}
	
	private static String[] convertToArray(String input){
		String[] arr = input.split(",");
		return arr;
	}

	public static boolean clearSharedPreference(Context context){
		PreferenceManager.getDefaultSharedPreferences(context)
						.edit()
						.remove(USER_CORRECT_ATTEMPTS)
						.commit();
		PreferenceManager.getDefaultSharedPreferences(context)
		.edit()
		.remove(USER_INCORRECT_ATTEMPTS)
		.commit();
		
		PreferenceManager.getDefaultSharedPreferences(context)
		.edit()
		.remove(USER_RATINGS)
		.commit();
		
		PreferenceManager.getDefaultSharedPreferences(context)
		.edit()
		.remove(DELETE_QS)
		.commit();
		
		PreferenceManager.getDefaultSharedPreferences(context)
		.edit()
		.remove(USER_POINTS)
		.commit();
	   return true;
	    }
/*
	//add Ratings
		public static boolean addRatingsPreferences(Context context,HashMap<String,Float> ratings){
			JSONObject jsonObject = new JSONObject(ratings);
	        String jsonString = jsonObject.toString();
	        
			PreferenceManager.getDefaultSharedPreferences(context)
			.edit()
			.remove("USER_RATINGS")
			.putString(USER_RATINGS, jsonString)
			.commit();
			return true;
		}
		
		//Get ratings
		public static HashMap<String,Float> getRatingsPreferences(Context context){
			HashMap<String,Float> outputMap = new HashMap<String,Float>();
			
			 try{
				
	        	String jsonString1 = PreferenceManager.getDefaultSharedPreferences(context)
	    				.getString(USER_RATINGS,(new JSONObject()).toString());
	        	JSONObject jsonObject = new JSONObject(jsonString1);
	        	Iterator<String> keysItr = jsonObject.keys();
	            while(keysItr.hasNext()) {
	                   String key = keysItr.next();
	                   Float value = (Float) jsonObject.get(key);
	                   outputMap.put(key, value);
	               }
	          }catch(Exception e){
	            e.printStackTrace();
	        }
	        return outputMap;
			
		}
		*/
}
