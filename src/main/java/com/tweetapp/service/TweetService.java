package com.tweetapp.service;

import com.tweetapp.exception.InvalidTweetMessageException;
import com.tweetapp.exception.TweetNotFoundException;
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

    public Tweet postANewTweet(Tweet tweet, User user) throws InvalidTweetMessageException {
        String tweetMessage = tweet.getMessage();
        if (tweetMessage == null || tweetMessage.trim().length() == 0) {
            throw new InvalidTweetMessageException("Tweet cannot be empty");
        }
        tweet.setUserId(user.getId());
        tweet.setUsername(user.getUsername());
        tweet.setListOfLikes(new HashMap<String, Boolean>());
        tweet.setTweetedAt(LocalDateTime.now());
        Tweet newTweet =  this.tweetRepository.insert(tweet);
        return newTweet;
    }

    public Tweet updateTweet(Tweet tweet, User user) throws InvalidTweetMessageException, TweetNotFoundException {
        String tweetMessage = tweet.getMessage();
        if (tweetMessage == null || tweetMessage.trim().length() == 0) {
            throw new InvalidTweetMessageException("Tweet cannot be empty");
        }
        Tweet originalTweet = this.tweetRepository.findTweetByIdAndUser(tweet.getId(), user.getId());
        System.out.println(originalTweet);
        if (originalTweet == null) {
            throw new TweetNotFoundException("Requested Tweet does not exist. Please check the request parameters.");
        }
        originalTweet.setMessage(tweetMessage);
        originalTweet.setListOfTags(tweet.getListOfTags());
        Tweet updatedTweet = this.tweetRepository.save(originalTweet);
        return updatedTweet;
    }

    public String deleteTweet(String id, User user) throws TweetNotFoundException {
        Tweet originalTweet = this.tweetRepository.findTweetByIdAndUser(id, user.getId());
        if (originalTweet == null) {
            throw new TweetNotFoundException("Requested Tweet does not exist. Please check the request parameters.");
        }
        tweetRepository.deleteById(originalTweet.getId());
        return "Success";
    }

    public Tweet replyTweet(String id, Tweet tweet, User user) throws InvalidTweetMessageException {
        tweet.setRepliedTo(id);
        Tweet tweetedReply = postANewTweet(tweet, user);
        return tweetedReply;
    }

    public Tweet updateListOfLikes(String id, User user) throws InvalidTweetMessageException {
        String userId = user.getId();
        Optional<Tweet> optionalTweet = this.tweetRepository.findById(id);
        if (optionalTweet.isEmpty()) {
            throw new InvalidTweetMessageException("Requested Tweet does not exist. Please check the request parameters.");
        }
        Tweet tweet = optionalTweet.get();
        HashMap<String, Boolean> originalList = tweet.getListOfLikes();
        if(originalList.get(userId) == null){
            originalList.put(userId, true);
        }
        tweet.setListOfLikes(originalList);
        Tweet updatedTweet = this.tweetRepository.save(tweet);
        return updatedTweet;
    }
}
