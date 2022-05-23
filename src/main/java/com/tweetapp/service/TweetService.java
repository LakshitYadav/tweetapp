package com.tweetapp.service;

import com.google.common.collect.Maps;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TweetService {

    @Autowired
    TweetRepository tweetRepository;

    public List<Tweet> getListOfAllTweets() {
        List<Tweet> allTweets =  this.tweetRepository.findAll();
        allTweets.sort(Comparator.comparing(Tweet::getTweetedAt));
        return allTweets;
    }

    public List<Tweet> getListOfAllReplies(String tweetId) {
        Optional<List<Tweet>> optionalTweets = this.tweetRepository.findTweetsByRepliedTo(tweetId);

        return optionalTweets.orElseGet(ArrayList::new);
    }

    public List<Tweet> findAllTweetsByUserId(String userId) {
        Optional<List<Tweet>> optionalTweets = this.tweetRepository.findTweetsByUserId(userId);

        return optionalTweets.orElseGet(ArrayList::new);
    }

    public Tweet postANewTweet(Tweet tweet, User user) {
        tweet.setUserId(user.getId());
        tweet.setUsername(user.getUsername());
        tweet.setListOfLikes(Maps.newHashMap());
        tweet.setTweetedAt(LocalDateTime.now());
        Tweet newTweet =  this.tweetRepository.insert(tweet);
        return newTweet;
    }

    public Tweet updateTweet(Tweet tweet, User user) {
        Tweet originalTweet = this.tweetRepository.findTweetByIdAndUser(tweet.getId(), user.getId());
        originalTweet.setTweetMessage(tweet.getTweetMessage());
        originalTweet.setListOfTags(tweet.getListOfTags());
        Tweet updatedTweet = this.tweetRepository.save(originalTweet);
        return updatedTweet;
    }

    public String deleteTweet(String id, User user) {
        Tweet originalTweet = this.tweetRepository.findTweetByIdAndUser(id, user.getId());
        tweetRepository.deleteById(originalTweet.getId());
        return "Success";
    }

    public Tweet replyTweet(String id, Tweet tweet, User user) {
        tweet.setRepliedTo(id);
        Tweet tweetedReply = postANewTweet(tweet, user);
        return tweetedReply;
    }

    public Tweet updateListOfLikes(String id, User user) {
        String userId = user.getId();
        Tweet tweet = this.tweetRepository.findById(id).get();
        HashMap<String, Boolean> originalList = tweet.getListOfLikes();
        if(originalList.get(userId) == null){
            originalList.put(userId, true);
        }
        tweet.setListOfLikes(originalList);
        Tweet updatedTweet = this.tweetRepository.save(tweet);
        return updatedTweet;
    }
}
