package com.tweetapp.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LoggedInUser {
    private static String userId;
    private static User user;
    private static boolean loginStatus = false;

    public static String getUserId() {
        return LoggedInUser.userId;
    }

    public static void setUserId(String userId) {
        LoggedInUser.userId = userId;
    }

    public static User getUser() {
        return LoggedInUser.user;
    }

    public static void setUser(User user) {
        LoggedInUser.user = user;
    }

    public static boolean getLoginStatus() {
        return LoggedInUser.loginStatus;
    }

    public static void setLoginStatus(boolean status) {
        LoggedInUser.loginStatus = status;
    }
}
