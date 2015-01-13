package org.ntip.androidApp.guessReel.repository;

import org.ntip.androidApp.guessReel.client.GameSvcApi;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = GameSvcApi.GAME_SVC_PATH)
public interface GameRepository extends MongoRepository<QuestionSet, String> {
	
public QuestionSet findBymId(int mId);

}
