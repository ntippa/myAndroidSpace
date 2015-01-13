package org.ntip.androidApp.guessReel.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;

public class QuestionSet implements Parcelable{
	
	
	private String id;
	
	private ArrayList<String> questions;
	
	private String correctAnswer;
	private String explanation;
	private float rating;// rating ex:1,2
	private int frequency; // no. of times played
	
	public QuestionSet() {
	}
	
	/*public QuestionSet(String id,Collection<String> questions,String correctAnswer,String explanation){
		super();
		this.id = id;
		this.questions = questions;
		this.correctAnswer = correctAnswer;
		this.explanation = explanation;
	}*/
	
	
	public ArrayList<String> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<String> questions) {
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {

		out.writeString(id);
		out.writeStringList(questions);
		out.writeString(correctAnswer);
		out.writeString(explanation);
		out.writeFloat(rating);
		out.writeInt(frequency);
		
	}
	
	public QuestionSet(Parcel in){
		
		id = in.readString();
		
		questions = new ArrayList<String>();
		in.readStringList(questions);
		
		correctAnswer = in.readString();
		
		explanation = in.readString();
		
		rating = in.readFloat();
		
		frequency = in.readInt();
		
	}

	public static final Parcelable.Creator<QuestionSet> CREATOR = 
			new Parcelable.Creator<QuestionSet>(){
				
				public QuestionSet createFromParcel(Parcel in){
					return new QuestionSet(in);
				}

				@Override
				public QuestionSet[] newArray(int size) {
					
					return new QuestionSet[size];
				}
				
				
				
			};
	
	
	
	
}	
