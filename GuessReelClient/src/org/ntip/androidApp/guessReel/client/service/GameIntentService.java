package org.ntip.androidApp.guessReel.client.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.ntip.androidApp.guessReel.client.GameSvc;
import org.ntip.androidApp.guessReel.client.GameSvcApi;
import org.ntip.androidApp.guessReel.client.GameUtility;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class GameIntentService extends IntentService {

	private static final String TAG = "GameIntentService";
	int points;
	
	public GameIntentService(){
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent arg0) {
		
		//HashMap<String,Float> updatedRatings = GameUtility.getRatingsPreferences(this);
		//HashMap<String,Float> updatedRatings = (HashMap<String,Float>)arg0.getSerializableExtra("updatedRatings");
		
		/*Log.d(TAG,"updatedRatings");
		for(Map.Entry<String, Float> entry: updatedRatings.entrySet()){
			Log.d(TAG,entry.getKey() + "::" + entry.getValue());
		}*/
		String ratings = (String)arg0.getStringExtra("updatedRatings");
		
		
		Log.d(TAG,"Started Service");
		String[] userData = GameUtility.getUserPreferences(this);
		Log.d(TAG,"User::" + userData[0] + "pass::" + userData[1] + "server::" + userData[2]);
		
		points = GameUtility.getPointsPreferences(this);
		Log.d(TAG,"User Points from Prefs" + points);
		
		String delete_qns = GameUtility.getQuestionsDelete(this); // id's marked for deletion
		String[] delete_qn_ids = delete_qns.split(",");
		
		
		final GameSvcApi svc = GameSvc.init(userData[2] , userData[0], userData[1]);
		
		try{
			svc.UpdateUserPoints(points);
			Log.d(TAG,"User points updated");
		}catch(Exception e){
			e.printStackTrace();
			Log.d(TAG,"error saving user points");
		}
		
		try{
			Log.d(TAG,"before saving ratings");
			svc.saveRatings(ratings);
		
		}catch(Exception e){
			e.printStackTrace();
			Log.d(TAG,"error saving ratings");
		}
		try{
		if(delete_qn_ids.length == 0){
			Log.d(TAG,"empty string");
		}else{
			
			Log.d(TAG,"Not empty string");
			for(int i = 1;i < delete_qn_ids.length; i++){
				Log.d(TAG,"qns marked for delete are" + delete_qn_ids[i]);
				svc.getQuestionSetForDelete(delete_qn_ids[i]);
			}
			
		}
		}catch(Exception e){
			Log.d(TAG,"Exception  deleting question sets");
		}
				
	}

}
