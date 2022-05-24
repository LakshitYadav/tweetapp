package com.tweetapp.exception;

public class InvalidTweetMessageException extends Exception{

    public InvalidTweetMessageException(String message) {
        super(message);
    }
}
