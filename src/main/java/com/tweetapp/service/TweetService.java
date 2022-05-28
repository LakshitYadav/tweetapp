package com.tweetapp.service;

import com.tweetapp.exception.InvalidTweetMessageException;
import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TweetService {

    @Autowired
    TweetRepository tweetRepository;

    public List<Tweet> getListOfAllTweets() {
        List<Tweet> allTweets =  this.tweetRepository.findAll(Sort.by(Sort.Direction.DESC, "repliedTo"));
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
        tweet.setNumberOfReplies(0);
        tweet.setListOfLikes(new HashMap<>());
        tweet.setTweetedAt(LocalDateTime.now());
        return this.tweetRepository.insert(tweet);
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
        return this.tweetRepository.save(originalTweet);
    }

    public String deleteTweet(String id, User user) throws TweetNotFoundException {
        Tweet originalTweet = this.tweetRepository.findTweetByIdAndUser(id, user.getId());
        if (originalTweet == null) {
            throw new TweetNotFoundException("Requested Tweet does not exist. Please check the request parameters.");
        }

        Optional<Tweet> optionalTweet = this.tweetRepository.findById(originalTweet.getRepliedTo());
        if(optionalTweet.isPresent()) {
            Tweet mainTweet = optionalTweet.get();
            mainTweet.setNumberOfReplies(mainTweet.getNumberOfReplies() - 1);
            this.tweetRepository.save(mainTweet);
        }

        tweetRepository.deleteById(originalTweet.getId());
        return "Success";
    }

    public Tweet replyTweet(String id, Tweet tweet, User user) throws InvalidTweetMessageException {
        tweet.setRepliedTo(id);
        Optional<Tweet> optionalTweet = this.tweetRepository.findById(id);
        if (optionalTweet.isEmpty()) {
            throw new InvalidTweetMessageException("Requested Action could not be completed. Please check the request parameters.");
        }
        Tweet tweetedReply = postANewTweet(tweet, user);

        // Updating the number of Replies in Database
        Tweet originalTweet = optionalTweet.get();
        originalTweet.setNumberOfReplies(originalTweet.getNumberOfReplies() + 1);
        this.tweetRepository.save(originalTweet);

        return tweetedReply;
    }

    public Tweet updateListOfLikes(String id, User user) throws InvalidTweetMessageException {
        Optional<Tweet> optionalTweet = this.tweetRepository.findById(id);
        if (optionalTweet.isEmpty()) {
            throw new InvalidTweetMessageException("Requested Tweet does not exist. Please check the request parameters.");
        }
        Tweet tweet = optionalTweet.get();
        HashMap<String, Boolean> originalList = tweet.getListOfLikes();
        originalList.putIfAbsent(user.getUsername(), true);
        tweet.setListOfLikes(originalList);
        return this.tweetRepository.save(tweet);
    }
}
