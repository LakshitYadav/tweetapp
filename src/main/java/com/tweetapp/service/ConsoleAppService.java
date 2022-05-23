package com.tweetapp.service;

import com.tweetapp.model.Gender;
import com.tweetapp.model.LoggedInUser;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ConsoleAppService {
    private final static String emailRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    private final static String usernameRegex = "^[A-Za-z]\\w{5,19}$";

    private final static String passwordRegex = "^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,20}$";

    final Scanner sc = new Scanner(System.in);

    public int getUserChoice(String input) {
        int userChoice;
        try {
            userChoice = Integer.parseInt(input.trim());
        }
        catch (NumberFormatException e) {
            userChoice = -99;
        }
        return userChoice;
    }

    public void showMenuForLoggedOutUser() {
        System.out.println("Please choose from the options below :");
        System.out.println("_____________________________________________\n" +
                "1. Register\n" +
                "2. Login\n" +
                "3. Forgot Password\n" +
                "4. Exit");
    }

    public void showMenuForLoggedInUser() {
        System.out.println("Please choose from the options below:");
        System.out.println(
                "____________________________________________\n" +
                        "1. Post a tweet\n" +
                        "2. View my tweets\n" +
                        "3. View all tweets\n" +
                        "4. View all users\n" +
                        "5. Reset password\n" +
                        "6. Logout");
    }


    public void printUsernameCriteria() {
        System.out.println("    * The first character of the username must be an alphabet. \n"
                + "    * It must contain only alphanumeric characters and underscores \n"
                + "    * It must contain at least 6 characters and at most 20 characters.\n");
    }

    public void printPasswordCriteria() {
        System.out.println("    * It must contain at least 8 characters and at most 20 characters.\n"
                + "    * It must contain at least one digit. \n"
                + "    * It must contain at least one upper case alphabet. \n"
                + "    * It must contain at least one lower case alphabet.\n"
                + "    * It must contain at least one special character which includes !@#$%&*()-+=^.\n"
                + "    * It must not contain any white space.\n");
    }

    public Map<String, String> getLoginUserCredentials() {
        Map<String, String> userCredentials = new HashMap<>();
        System.out.println("Enter Your Email");
        String email = sc.nextLine();
        userCredentials.put("email", email);

        System.out.println("Enter the Password");
        String password = sc.nextLine();
        userCredentials.put("password", password);

        return userCredentials;
    }

    public String getEmailDetails(){
        String email;
        while(true) {
            System.out.println("Enter your Email Address: ");
            email = sc.nextLine();
            if(email.matches(emailRegex)) return email;
            System.out.println("Please enter a valid email address.");
        }
    }

    public String getUsernameDetails(){
        String username;
        while(true) {
            System.out.println("Enter your username: ");
            printUsernameCriteria();
            username = sc.nextLine();
            if(username.matches(usernameRegex)) return username;
            System.out.println("Please enter a valid username.");
        }
    }

    public String getPasswordDetails(){
        String password;
        String confirmPassword;

        while(true) {
            System.out.println("Enter your password: ");
            printPasswordCriteria();
            password = sc.nextLine();
            if(password.matches(passwordRegex)) {
                while (true) {
                    System.out.println("Please confirm your password: ");
                    confirmPassword = sc.nextLine();
                    if (password.equals(confirmPassword)) return password;
                    System.out.println("Password and Confirm-Password do not match. Please try again");
                }
            }
            System.out.println("Your password does not match the required criteria.");
        }
    }

    public Gender getGenderDetails(){
        String gender;
        while(true) {
            System.out.println("Enter your gender (MALE / FEMALE): ");
            gender = sc.nextLine().toUpperCase();
            if(gender.equals("MALE") || gender.equals("FEMALE")) return Gender.valueOf(gender);
            System.out.println("Please enter 'MALE' or 'FEMALE'.");
        }
    }

    public Date getUserDateOfBirth(){
        Date dateOfBirth = new Date();
        String userChoice;
        do {
            System.out.println("Do you want to provide Date of Birth: Y/N");
            userChoice = sc.nextLine().trim().toUpperCase();

            switch (userChoice) {
                case "N": break;
                case "Y":
                    System.out.println("  i. Enter your year of birth in YYYY format (numbers only).");
                    System.out.print("\t ");
                    int year = sc.nextInt();

                    System.out.println("  ii. Enter your month of birth in MM format.");
                    System.out.print("\t ");
                    int month = sc.nextInt();

                    System.out.println("  iii. Enter your date of birth in DD format.");
                    System.out.print("\t ");
                    int date = sc.nextInt();
                    sc.nextLine();

                    String dob = date + "/" + month + "/" + year;

                    try {
                        dateOfBirth = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
                        userChoice = "N";
                    } catch (ParseException e) {
                        // e.printStackTrace();
                        System.out.println("Date is not entered correctly");
                    }
                    break;
                default:
                    System.out.println("Please select a valid value: Y or N");
            }
        } while(userChoice.equals("Y"));

        return dateOfBirth;
    }

    @SuppressWarnings("SameReturnValue")
    public String getUserAvatar(){
        return "https://thumbs.dreamstime.com/b/avatar-faceless-male-profile-vector-illustration-graphic-design-avatar-faceless-male-profile-138082635.jpg";
    }

    public User getUserRegistrationDetails(){

        System.out.println("Enter First Name: ");
        String firstName = sc.nextLine();

        System.out.println("Enter Last Name: ");
        String lastName = sc.nextLine();

        String email = getEmailDetails();

        String username = getUsernameDetails();

        String password = getPasswordDetails();

        Gender gender = getGenderDetails();

        Date dateOfBirth = getUserDateOfBirth();

        String avatarLink = getUserAvatar();

        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setGender(gender);
        newUser.setDateOfBirth(dateOfBirth);
        newUser.setAvatarLink(avatarLink);
        newUser.setCreatedAt(LocalDateTime.now());

        return newUser;
    }

    public Tweet postATweet(){
        Tweet newTweet = new Tweet();
        while(true) {
            System.out.println("Enter tweet message (Max Limit: 144 characters): ");
            String tweetMessage = sc.nextLine();
            if (tweetMessage.length() <= 144) {
                newTweet.setMessage(tweetMessage);
                break;
            }
            System.out.println("Tweet exceeded 144 characters.");
        }
        newTweet.setUserId(LoggedInUser.getUserId());
        newTweet.setUsername(LoggedInUser.getUser().getUsername());
        newTweet.setTweetedAt(LocalDateTime.now());

        return newTweet;
    }
}
