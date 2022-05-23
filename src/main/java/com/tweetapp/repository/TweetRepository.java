package com.tweetapp.repository;

import com.tweetapp.model.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends MongoRepository<Tweet, String> {

    Optional<List<Tweet>> findTweetsByUserId(String userId);

    Optional<List<Tweet>> findTweetsByRepliedTo(String repliedTo);

    //To find the tweet where tweetId=id AND userId=userId
    @Query("{$and : [{id: ?0}, {userId: ?1}]}")
    Tweet findTweetByIdAndUser(String tweetId, String userId);
}
