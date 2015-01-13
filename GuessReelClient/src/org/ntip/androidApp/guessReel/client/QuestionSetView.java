package org.ntip.androidApp.guessReel.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;


import org.ntip.androidApp.guessReel.client.model.QuestionSet;
import org.ntip.androidApp.guessReel.client.ui.LoginScreenActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class QuestionSetView extends Activity {
	
	@InjectView(R.id.videoList)
	protected ListView videoList_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);

		ButterKnife.inject(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		refreshVideos();
	}

	private void refreshVideos() {
		
		final GameSvcApi svc = GameSvc.getOrShowLogin(this);

		if (svc != null) {
			CallableTask.invoke(new Callable<Collection<QuestionSet>>() {

				@Override
				public Collection<QuestionSet> call() throws Exception {
					return svc.loadGame();
				}
			}, new TaskCallback<Collection<QuestionSet>>() {

				@Override
				public void success(Collection<QuestionSet> result) {
					List<String> qSets = new ArrayList<String>();
					for (QuestionSet qs : result) {
						qSets.addAll(qs.getQuestions());
					}
					videoList_.setAdapter(new ArrayAdapter<String>(
							QuestionSetView.this,
							android.R.layout.simple_list_item_1, qSets));
				}

				@Override
				public void error(Exception e) {
					Toast.makeText(
							QuestionSetView.this,
							"Unable to fetch the video list, please login again.",
							Toast.LENGTH_SHORT).show();

					startActivity(new Intent(QuestionSetView.this,
							LoginScreenActivity.class));
				}
			});
		}
	}

}
