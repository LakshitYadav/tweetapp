package com.tweetapp.repository;

import com.tweetapp.model.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Lakshit Yadav
 */

@Repository
public interface TweetRepository extends MongoRepository<Tweet, String> {

    Optional<List<Tweet>> findTweetsByUserId(String userId);
}
