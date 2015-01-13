package org.ntip.androidApp.guessReel.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.ntip.androidApp.guessReel.auth.User;
import org.ntip.androidApp.guessReel.repository.Points;
import org.ntip.androidApp.guessReel.repository.QuestionSet;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;


/*
 * author:Nalini,10/22/14
 * This interface defines the API for Game service GameSvc.
 * This interface provides contract for client/server intercations and  is annotated with Retrofit 
 * 
 */

public interface GameSvcApi {
	
	public static final String GAME_PASSWORD_PARAMETER = "password";

	public static final String GAME_USERNAME_PARAMETER = "username";

	
	public static final String TOKEN_PATH = "/oauth/token";
	
	// The path where we expect the VideoSvc to live
	public static final String GAME_SVC_PATH = "/game";
	public static final String POINTS_SVC_PATH = "/game/points";
	public static final String USER_SVC_PATH = "/game/user";

	
	@GET(GAME_SVC_PATH)
	public Collection<QuestionSet> loadGame();
	
	@POST(GAME_SVC_PATH)
	public Void saveRatings(@Body String updatedRatings);
	
	@GET(GAME_SVC_PATH + "/{id}")
	public QuestionSet getQuestionSetForDelete(@Path("id") String id);
	
		@GET(POINTS_SVC_PATH)
	public Points getUserPoints();
	
		@POST(POINTS_SVC_PATH)
	public Void UpdateUserPoints(@Body int points);
		
		@FormUrlEncoded
		@POST(USER_SVC_PATH)
		public User getNewUser(@Field("username")String username, @Field("password") String password);
		
		/*@DELETE(GAME_SVC_PATH + "/{id}")
		public Void getQuestionSetForDelete(@Path("id") String id);*/			
	
	
}
