package com.tweetapp.service;

import com.mongodb.MongoWriteException;
import com.tweetapp.model.LoggedInUser;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author Lakshit Yadav
 */

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
        User registeredUser;
        try {
            registeredUser = userRepository.insert(user);
            System.out.println("Congratulations!!! You are now registered \n");
            return registeredUser;
        }
        catch (Exception e) {
            if(e instanceof DuplicateKeyException || e instanceof MongoWriteException) {
                if (e.getMessage().contains("email dup key")) {
                    System.out.println("A user with same email address already exists");
                }
                else if (e.getMessage().contains("username dup key")) {
                    System.out.println("A user with same username already exists");
                    String newUsername = consoleAppService.getUsernameDetails();
                    user.setUsername(newUsername);
                    registeredUser = registerUser(user);
                    return registeredUser;
                }
            }
            System.out.println("Error : " + e);
        }
        return null;
    }

    public void loginUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getPassword().equals(password)) {
                LoggedInUser.setLoginStatus(true);
                LoggedInUser.setUser(user);
                LoggedInUser.setUserId(user.getId());
                System.out.println("You have logged in successfully !!!!\n");
            } else {
                System.out.println("Your Password is incorrect. Please try again.\n");
            }
        } else {
            System.out.println("User does not exist in our records. Please register as new user.\n");
        }
    }
}
