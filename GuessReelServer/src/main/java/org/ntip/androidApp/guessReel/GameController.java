package org.ntip.androidApp.guessReel;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.ntip.androidApp.guessReel.auth.User;
import org.ntip.androidApp.guessReel.client.GameSvcApi;
import org.ntip.androidApp.guessReel.repository.GameRepository;
import org.ntip.androidApp.guessReel.repository.Points;
import org.ntip.androidApp.guessReel.repository.QuestionSet;
import org.ntip.androidApp.guessReel.repository.UserPointsRepository;
import org.ntip.androidApp.guessReel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.http.Body;




import retrofit.http.Field;
import retrofit.http.POST;







import com.google.common.collect.Lists;

@Controller
public class GameController {

	@Autowired
	private GameRepository repo;
	
	@Autowired
	private UserPointsRepository pointsRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	private int NUM_GAME_QUESTIONS;
	
	
		/*
	 * make assumptions about question set db:
	 *  qns per game:10
	 * 3 incorrect will loose game
	 * win: 7 correct
	 */
	@RequestMapping(value=GameSvcApi.GAME_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<QuestionSet> loadGame(){
		
		Collection<QuestionSet> mySet = new HashSet<QuestionSet>();
		int count = (int)repo.count();// count records.assumed 30 to start
		int MIN = 3;
		
		NUM_GAME_QUESTIONS = 6; // FOR DEMO
		//create set of 10 random numbers
		int[] myArray = new int[10];
		
		for(int i = 0 ; i < NUM_GAME_QUESTIONS ; i++){
			
			myArray[i] = new Random().nextInt((count - MIN)  + 1) + MIN;//.add(Integer.valueOf(new Random().nextInt((count - MIN)  + 1) + MIN)); 
			
		}
	
	
		//retrieve questions from db
		for(int obj : myArray){
			
					if(repo.findBymId(obj) == null){
							System.out.println("record Not Found for ID::" + obj);
					}else{
							System.out.println("record Found for ID::" + obj);
							mySet.add(repo.findBymId(obj));
					}//if
		}//for
	
		
		return mySet;
}//loadGame

	@RequestMapping(value=GameSvcApi.GAME_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Void> saveRatings(@RequestBody String ratings){
		HashMap<String,String> ratingsMap = new HashMap<String,String>();
		
		try{
		String[] temp = ratings.split(",");
		
		for(int i = 1;i < temp.length - 1; i = i+2){
			ratingsMap.put(temp[i], temp[i+1]);
		}
		
		for(Map.Entry<String, String> entry: ratingsMap.entrySet()){
			String id = entry.getKey();
			String rating_str = entry.getValue();
			float rating = Float.valueOf(rating_str);
			QuestionSet qs = repo.findOne(id);
			if(qs!= null)
				System.out.println("found qs" + qs.getCorrectAnswer() + qs.getRating());
			qs.setRating(rating);
			repo.save(qs);	// saving the entire questionset entity instead of just the ratings
			System.out.println("rating saved");
			
		}//for
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
//GET  User Points
	@RequestMapping(value=GameSvcApi.POINTS_SVC_PATH, method=RequestMethod.GET)
	public  @ResponseBody Points getUserPoints(Principal p){
	
	Points foundUser;
	String principal = p.getName();
	System.out.println("Enered getUserPoints() in controller");
	
	
	if(!pointsRepo.exists(principal)) {
		
		System.out.println("User does not exist in DB");
		System.out.println("New User...saving to repo");
		pointsRepo.save(new Points(principal,0));//MAYBE THIS HAS TO DIRECTED TO REGISTRATION
		
		System.out.println("Test the saved user");
		foundUser = pointsRepo.findByUsername(principal);
		System.out.println(" Saved foundUser" + foundUser.getUsername()+ "Points" + foundUser.getPoints());
	} else {
		foundUser = pointsRepo.findByUsername(principal);
		System.out.println("findByUsername::" +"username::" + foundUser.getUsername().toString() +":::@POints" +  foundUser.getPoints());
		System.out.println("TEST:findByUsername SUUCESS");
	}
	
	return foundUser;
	
}

	@RequestMapping(value=GameSvcApi.POINTS_SVC_PATH , method = RequestMethod.POST)
	@ResponseBody
	public  ResponseEntity<Void> updateUserPoints(@RequestBody int points,Principal p){
	
	Points userPoints;
	String principal = p.getName();
	/*if(!user.equals(principal)){
		System.out.println("input user:: " + user + "and principal::" + principal + " are different");
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}*/
	try{
	userPoints = pointsRepo.findByUsername(principal);
	userPoints.setPoints(points);
	pointsRepo.save(userPoints);
	
	}catch(Exception e){
		System.out.println("Exception during user points save");
		e.printStackTrace();
		
	}
	
	return new ResponseEntity<Void>(HttpStatus.OK);
	
}
				


/*//GET /video/{id}
@RequestMapping(value="/game/{id}",method=RequestMethod.GET)
public @ResponseBody QuestionSet getQuestionSetById(@PathVariable("id") String id){
	return repo.findOne(id);
}*/

//DELETE QUESION SETS MARKED FOR DELETION
@RequestMapping(value=GameSvcApi.GAME_SVC_PATH + "/{id}",method=RequestMethod.GET)
public @ResponseBody ResponseEntity<Void> getQuestionSetForDelete(@PathVariable("id") String id){
	try{
			repo.delete(id);
		
	}catch(Exception e){
		e.printStackTrace();
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}
	return new ResponseEntity<Void>(HttpStatus.OK);
}

@RequestMapping(value = GameSvcApi.USER_SVC_PATH,method=RequestMethod.POST)
public @ResponseBody ResponseEntity<User> getNewUser(@Field("username")String username, @Field("password") String password){
	
	User savedUser = userRepo.save(new User(username,password));
	pointsRepo.save(new Points(username,0));// new user points = 0
	System.out.println("savedUser::" + savedUser.getUsername() +"::" + savedUser.getPassword());
	return new ResponseEntity<User>(savedUser,HttpStatus.OK);
	
	
}
}//GameController



