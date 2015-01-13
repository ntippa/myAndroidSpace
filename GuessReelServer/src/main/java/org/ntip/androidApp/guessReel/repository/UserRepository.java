package org.ntip.androidApp.guessReel.repository;

import org.ntip.androidApp.guessReel.auth.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface UserRepository extends MongoRepository<User, String> {

	public User findByUsername(String username);
}
