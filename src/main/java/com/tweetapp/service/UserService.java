package com.tweetapp.service;

import com.mongodb.MongoWriteException;
import com.tweetapp.exception.InvalidUserCredentialsException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.LoggedInUser;
import com.tweetapp.model.User;
import com.tweetapp.model.UserCredentials;
import com.tweetapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    ConsoleAppService consoleAppService;

    @Autowired
    UserRepository userRepository;

    public List<User> getListOfAllUsers() {
        return userRepository.findAll();
    }

    public User registerUser(User user){
        user.setCreatedAt(LocalDateTime.now());
        User registeredUser = userRepository.insert(user);
        System.out.println("Congratulations!!! You are now registered \n");
        return registeredUser;
    }

    public UserCredentials loginUser(String email, String password) throws InvalidUserCredentialsException {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        UserCredentials userDetails;
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getPassword().equals(password)) {
                userDetails = new UserCredentials(email, null, user.getUsername());
                System.out.println("You have logged in successfully !!!!\n");
                return userDetails;
            } else {
                throw new InvalidUserCredentialsException("Requested User records does not exist.");

            }
        } else {
            throw new InvalidUserCredentialsException("Requested User records does not exist.");
        }
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
