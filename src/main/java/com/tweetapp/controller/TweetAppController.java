package com.tweetapp.controller;

import com.mongodb.MongoWriteException;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.model.UserCredentials;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetAppController {

    @Autowired
    TweetService tweetService;

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User newUser) {
        User registeredUser = new User();
        try {
            registeredUser = userService.registerUser(newUser);
        }
        catch (Exception e) {
            if(e instanceof DuplicateKeyException || e instanceof MongoWriteException) {
                if (e.getMessage().contains("email dup key")) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("A user with same email address already exists");
                }
                else if (e.getMessage().contains("username dup key")) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("A user with same username already exists");
                }
            }
        }
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> loginUser(@RequestBody UserCredentials userCredentials)
    {
        boolean isUserLoggedIn = userService.loginUser(userCredentials.getId(), userCredentials.getPassword());

        if(isUserLoggedIn) {
            return ResponseEntity.ok(isUserLoggedIn);
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(isUserLoggedIn);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tweet>> getAllTweets() {
        return ResponseEntity.ok(tweetService.getListOfAllTweets());
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getListOfAllUsers());
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        Optional<User> user =  userService.findUserByUsername(username);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Tweet>> getAllTweetsByUsername(@PathVariable("username") String username) {
        Optional<User> user =  userService.findUserByUsername(username);

        if (user.isPresent()) {
            String userId = user.get().getId();
            List<Tweet> tweets = tweetService.findAllTweetsByUser(userId);
            return ResponseEntity.ok(tweets);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{username}/add")
    public ResponseEntity<Tweet> postTweetByUsername(@RequestBody Tweet tweet, @PathVariable("username") String username) {
        Optional<User> user = userService.findUserByUsername(username);
        if(user.isPresent()) {
            Tweet newTweet = tweetService.postANewTweet(tweet, user.get());
            return ResponseEntity.ok(newTweet);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{username}/update/{id}")
    public ResponseEntity<Tweet> updateTweetByUsername(@RequestBody Tweet tweet, @PathVariable("username") String username, @PathVariable("id") String id) {
        Optional<User> user = userService.findUserByUsername(username);
        if(user.isPresent()) {
            tweet.setId(id);
            Tweet updatedTweet = tweetService.updateTweet(tweet, user.get());
            if(updatedTweet == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(null);
            }
            return ResponseEntity.ok(updatedTweet);
        }
        return ResponseEntity.internalServerError().body(null);
    }

    @DeleteMapping("/{username}/delete/{id}")
    public ResponseEntity<String> deleteTweetByUsername(@PathVariable("username") String username, @PathVariable("id") String id) {
        Optional<User> user = userService.findUserByUsername(username);
        if(user.isPresent()) {
            try{
                String success = tweetService.deleteTweet(id, user.get());
                if(success == null) {
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body(null);
                }
                return ResponseEntity.ok(success);
            }
            catch (Exception e) {
                System.out.println(e);
                return ResponseEntity.internalServerError().body(null);
            }
        }
        return ResponseEntity.internalServerError().body(null);
    }

    @PostMapping("/{username}/add/{id}")
    public ResponseEntity<Tweet> replyTweetByUsername(@RequestBody Tweet tweet, @PathVariable("username") String username, @PathVariable("id") String id) {
        Optional<User> user = userService.findUserByUsername(username);
        if(user.isPresent()) {
            Tweet tweetedReply = tweetService.replyTweet(id, tweet, user.get());
            return ResponseEntity.ok(tweetedReply);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{username}/like/{id}")
    public ResponseEntity<Tweet> likeTweetByUsername(@PathVariable("username") String username, @PathVariable("id") String id) {
        Optional<User> user = userService.findUserByUsername(username);
        if(user.isPresent()) {
            Tweet updatedTweet = tweetService.updateListOfLikes(id, user.get());
            return ResponseEntity.ok(updatedTweet);
        }
        return ResponseEntity.notFound().build();
    }

}
