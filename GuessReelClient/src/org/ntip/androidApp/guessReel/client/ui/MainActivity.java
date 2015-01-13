package org.ntip.androidApp.guessReel.client.ui;


import java.util.ArrayList;
import java.util.Collection;

import org.ntip.androidApp.guessReel.client.GameUtility;
import org.ntip.androidApp.guessReel.client.GameUtility.status;
import org.ntip.androidApp.guessReel.client.model.QuestionSet;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	static final int NUM_ITEMS = 10;
	static final String TAG = "MainActivity.class";
	
	
	
    ViewPager mPager;
     ArrayList<QuestionSet> mQSets;
     int userPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);
      
        
        mQSets = getIntent().getExtras().getParcelableArrayList("QuestionSets");//get questionSets
        
        GameUtility.NUM_QUESTIONS = mQSets.size();
        Log.d(TAG,"NUM_QUESTIONS" + GameUtility.NUM_QUESTIONS);
        
        if(mQSets == null){
        	Log.d(TAG,"mQSets is not null");
        }else{
        	Log.d(TAG,mQSets.get(0).getQuestions().get(0).toString());
        	Log.d(TAG,mQSets.get(0).getExplanation().toString());
        }
        /*//final int userPoints = getIntent().getExtras().getInt("userPoints");
        userPoints = GameUtility.getPointsPreferences(MainActivity.this);
        Log.d(TAG,"UserPoints::" + userPoints);*/
        
        
        FragmentManager fm = getSupportFragmentManager();
      

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new FragmentStatePagerAdapter(fm){
        	@Override
        	public int getCount(){
        		return mQSets.size();
        	}
        	
        	@Override
        	public Fragment getItem(int pos){
        		//pos =0;
        		
        		QuestionSet result_qs = new QuestionSet();
        		//mPager.setCurrentItem(0);
        		//String[] questions;
        		result_qs = mQSets.get(pos);
        		if(result_qs == null){
        			Log.d(TAG,"result_qs is null");
        			result_qs = mQSets.get(0);
        		}
        		return QuestionSetListFragment.newInstance(result_qs,pos);	
        	}
        });

       
    } //onCreate()  

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//onPauseSoundConfig(mSoundPool);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//onResumeSoundConfig();
	}
	
}//class
