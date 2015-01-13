package org.ntip.androidApp.guessReel.repository;


import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;


@Repository
public interface UserPointsRepository extends MongoRepository<Points, String> {

	public Points findByUsername(String username);
	
	public Collection<Points> findByPoints(int points);

}
