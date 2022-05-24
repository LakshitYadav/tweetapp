package com.tweetapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiExceptionResponse {

    private final String message;
    private final int status;
    private final HttpStatus error;
    private final LocalDateTime timestamp;

}
