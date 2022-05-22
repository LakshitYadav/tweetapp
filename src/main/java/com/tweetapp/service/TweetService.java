package com.tweetapp.service;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Lakshit Yadav
 *
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

    public List<Tweet> findAllTweetsByUser(String userId) {
        Optional<List<Tweet>> optionalTweets = this.tweetRepository.findTweetsByUserId(userId);

        return optionalTweets.orElseGet(ArrayList::new);
    }

    public Tweet postANewTweet(Tweet tweet, User user) {
        tweet.setUserId(user.getId());
        tweet.setUsername(user.getUsername());
        tweet.setListOfReplies(Collections.EMPTY_LIST);
        tweet.setListOfLikes(Collections.EMPTY_LIST);
        tweet.setTweetedAt(LocalDateTime.now());
        Tweet newTweet =  this.tweetRepository.insert(tweet);
        return newTweet;
    }

    public Tweet updateTweet(Tweet tweet, User user) {
        List<Tweet> listOfTweetsByUser = findAllTweetsByUser(user.getId());
        for (Tweet eachTweet : listOfTweetsByUser) {
            if(eachTweet.getId().equals(tweet.getId())) {
                eachTweet.setTweetMessage(tweet.getTweetMessage());
                Tweet updatedTweet = this.tweetRepository.save(eachTweet);
                return updatedTweet;
            }
        }
        return null;
    }

    public String deleteTweet(String id, User user) {
        List<Tweet> listOfTweetsByUser = findAllTweetsByUser(user.getId());
        for (Tweet eachTweet : listOfTweetsByUser) {
            if(eachTweet.getId().equals(id)) {
                tweetRepository.deleteById(id);
                return "Success";
            }
        }
        return null;
    }

    public Tweet replyTweet(String id, Tweet tweet, User user) {
        tweet.setRepliedTo(id);
        Tweet tweetedReply = postANewTweet(tweet, user);
        return tweetedReply;
    }

    public Tweet updateListOfLikes(String id, User user) {
        Tweet tweet = this.tweetRepository.findById(id).get();
        List<String> originalList = tweet.getListOfLikes();
        originalList.add(user.getId());
        tweet.setListOfLikes(originalList);
        Tweet updatedTweet = this.tweetRepository.save(tweet);
        return updatedTweet;
    }

}
