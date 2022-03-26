package com.tweetapp.service;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Lakshit Yadav
 */

@Service
public class TweetService {

    @Autowired
    TweetRepository tweetRepository;

    public List<Tweet> getListOfAllTweets() {
        List<Tweet> allTweets =  this.tweetRepository.findAll();
        allTweets.sort(Comparator.comparing(Tweet::getTweetedAt));
        return allTweets;
    }

    public void postANewTweet(Tweet tweet) {
        try {
            Tweet newTweet =  this.tweetRepository.insert(tweet);
        }
        catch (Exception e) {
            System.out.println("Error : " + e);
        }
    }

    public List<Tweet> findAllTweetsByUser(String userId) {
        Optional<List<Tweet>> optionalTweets = this.tweetRepository.findTweetsByUserId(userId);

        return optionalTweets.orElseGet(ArrayList::new);
    }
}
