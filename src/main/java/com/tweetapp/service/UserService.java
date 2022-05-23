package com.tweetapp.service;

import com.mongodb.MongoWriteException;
import com.tweetapp.model.LoggedInUser;
import com.tweetapp.model.User;
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

    public boolean loginUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getPassword().equals(password)) {
                LoggedInUser.setLoginStatus(true);
                LoggedInUser.setUser(user);
                LoggedInUser.setUserId(user.getId());
                System.out.println("You have logged in successfully !!!!\n");
                return true;
            } else {
                System.out.println("Your Password is incorrect. Please try again.\n");
                return false;
            }
        } else {
            System.out.println("User does not exist in our records. Please register as new user.\n");
            return false;
        }
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
