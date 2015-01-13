package org.ntip.androidApp.guessReel.repository;

import org.springframework.data.annotation.Id;

import com.google.common.base.Objects;

public class Points {
	
	@Id
	private String username;
	
	private int points;
	
	/*public Points(){
	}
	*/
	public Points(String username, int points){
		super();
		this.username = username;
		this.points = points;
	}

	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(username,points);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Points) {
			Points other = (Points) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(username, other.username)
					&& Objects.equal(points, other.points);
					
		} else {
			return false;
		}
	}
	
}
