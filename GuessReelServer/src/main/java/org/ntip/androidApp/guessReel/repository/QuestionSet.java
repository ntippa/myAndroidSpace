package org.ntip.androidApp.guessReel.repository;

import java.util.Collection;
import java.util.HashSet;




import org.springframework.data.annotation.Id;

import com.google.common.base.Objects;

public class QuestionSet {
	
	@Id
	private String id;
	
	private int mId;
	private Collection<String> questions = new HashSet<String>();
	private String correctAnswer;
	private String explanation;
	private float rating;// rating ex:1,2
	private int frequency; // no. of times played
	
	public QuestionSet() {
	}
	
	public QuestionSet(String id,Collection<String> questions,String correctAnswer,String explanation,int mID){
		super();
		this.id = id;
		this.questions = questions;
		this.correctAnswer = correctAnswer;
		this.explanation = explanation;
		this.mId = mID;
	}
	
	
	public Collection<String> getQuestions() {
		return questions;
	}

	public void setQuestions(Collection<String> questions) {
		this.questions = questions;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	/**
	 * @return the rating
	 */
	public float getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}

	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	
	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(correctAnswer,explanation);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QuestionSet) {
			QuestionSet other = (QuestionSet)obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(correctAnswer, other.correctAnswer)
					&& Objects.equal(explanation, other.explanation);
					
		} else {
			return false;
		}
	}

	
	
	
	
}	
