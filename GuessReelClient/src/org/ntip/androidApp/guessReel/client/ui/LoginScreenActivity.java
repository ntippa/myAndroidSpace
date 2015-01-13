package org.ntip.androidApp.guessReel.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;


import org.ntip.androidApp.guessReel.client.GameUtility;
import org.ntip.androidApp.guessReel.client.model.QuestionSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 
 * This application uses ButterKnife. AndroidStudio has better support for
 * ButterKnife than Eclipse, but Eclipse was used for consistency with the other
 * courses in the series. If you have trouble getting the login button to work,
 * please follow these directions to enable annotation processing for this
 * Eclipse project:
 * 
 * http://jakewharton.github.io/butterknife/ide-eclipse.html
 * 
 */
public class LoginScreenActivity extends FragmentActivity {
	
	static final String TAG = " LoginScreenActivity.class";
	
	private MainFragment mainFragment;

	/*@InjectView(R.id.userName)
	protected EditText userName_;

	@InjectView(R.id.password)
	protected EditText password_;

	@InjectView(R.id.server)
	protected EditText server_;*/
	/*
	Menu menu;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);

		//ButterKnife.inject(this);
		if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new MainFragment();
            getSupportFragmentManager()
            .beginTransaction()
            .add(android.R.id.content, mainFragment)
            .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (MainFragment) getSupportFragmentManager()
            .findFragmentById(android.R.id.content);
        }
	}

	/*@OnClick(R.id.loginButton)
	public void login() {
		
		final Collection<QuestionSet> qsData;
		String user = userName_.getText().toString();
		String pass = password_.getText().toString();
		String server = server_.getText().toString();
		
		
		if(user.equalsIgnoreCase("") || pass.equalsIgnoreCase("")){
			Toast.makeText(this, "Oops!! Please enter your credentials", Toast.LENGTH_SHORT).show();
			//startActivity(new Intent(this,LoginScreenActivity.class));	
			this.recreate();
		}else{
		

		String credentials = user + "," + pass + "," + server;
		
		if(!GameUtility.addUserPreferences(this,credentials))
			Log.d(TAG,"Unable to store user credentials");
		else{
			Log.d(TAG,"TESTING:User credentials stored in" + this.getLocalClassName());
			String[] userData = GameUtility.getUserPreferences(this);
			Log.d(TAG,"User::" + userData[0] + "pass::" + userData[1] + "server::" + userData[2]);
		}
		
		Intent i = new Intent(LoginScreenActivity.this,
				LaunchActivity.class);
		startActivity(i);
		}	
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.user_login, menu);
		return super.onCreateOptionsMenu(menu);
	}
   
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.register:
			Log.d(TAG,"new user");
			startActivity(new Intent(LoginScreenActivity.this,RegisterActivity.class));
			Log.d(TAG," after calling registeractivity");
			//GameSvcApi register_svc = GameSvc.init()
		}
		return super.onOptionsItemSelected(item);
	}

	*/
	

}
