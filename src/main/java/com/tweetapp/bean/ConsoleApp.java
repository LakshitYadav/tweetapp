package com.tweetapp.bean;

import com.tweetapp.model.LoggedInUser;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.service.ConsoleAppService;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Lakshit Yadav
 */

@Component
public class ConsoleApp {

    @Autowired
    ConsoleAppService consoleAppService;

    @Autowired
    UserService userService;

    @Autowired
    TweetService tweetService;

    public void runConsoleApplication() throws InputMismatchException {
        Scanner sc = new Scanner(System.in);

        String userChoice;
        boolean userLoggedIn;
        System.out.println("\n<--------- Welcome to the Tweet Application --------->\n");
        do {
            userLoggedIn = LoggedInUser.getLoginStatus();
            if (userLoggedIn) {
                consoleAppService.showMenuForLoggedInUser();
                userChoice = sc.nextLine();
                int finalUserChoice = consoleAppService.getUserChoice(userChoice);
                switch (finalUserChoice) {
                    case 1 :
                        Tweet newTweet = consoleAppService.postATweet();
                        tweetService.postANewTweet(newTweet);
                        System.out.println("Tweet Posted Successfully\n");
                        break;
                    case 2 :
                        List<Tweet> myTweets = tweetService.findAllTweetsByUser(LoggedInUser.getUserId());
                        if(myTweets.isEmpty())
                            System.out.println("You haven't tweeted anything yet!!!");
                        else {
                            System.out.println("My Tweets : \n");
                            myTweets.forEach(System.out::println);
                        }
                        break;
                    case 3 :
                        List<Tweet> allTweets = tweetService.getListOfAllTweets();
                        if(allTweets.isEmpty())
                            System.out.println("There are no tweets to view at the moment");
                        else {
                            System.out.println("All tweets : \n");
                            allTweets.forEach(System.out::println);
                        }
                        break;
                    case 4 :
                        List<User> allUsers = userService.getListOfAllUsers();
                        System.out.println("All users\n");
                        allUsers.forEach(System.out::println);
                        break;
                    case 5 :
                        System.out.println("This feature is not yet available. Thank you for your patience.\n");
                        break;
                    case 6 :
                        LoggedInUser.setLoginStatus(false);
                        LoggedInUser.setUserId(null);
                        LoggedInUser.setUser(null);
                        System.out.println("You are now logged out.\n");
                        break;
                    default:
                        System.out.println("Please enter a valid choice\n");
                        break;
                }
            } else {
                consoleAppService.showMenuForLoggedOutUser();
                userChoice = sc.nextLine();
                int finalUserChoice = consoleAppService.getUserChoice(userChoice);
                switch (finalUserChoice) {
                    case 1 :
                        User newUser = consoleAppService.getUserRegistrationDetails();
                        userService.registerUser(newUser);
                        break;
                    case 2 :
                        Map<String, String> userCredentials = consoleAppService.getLoginUserCredentials();
                        userService.loginUser(userCredentials.get("email"), userCredentials.get("password"));
                        break;
                    case 3 :
                        System.out.println("This feature is not yet available. Thank you for your patience.\n");
                        break;
                    case 4 :
                        System.out.println("Closing the Application\n");
                        sc.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Please enter a valid choice\n");
                        break;
                }
            }
        } while (true);
    }
}
