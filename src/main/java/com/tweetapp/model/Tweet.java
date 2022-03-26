package com.tweetapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lakshit Yadav
 */

@Data
@NoArgsConstructor
@Document(collection = "tweet")
public class Tweet {

    @Id
    private String id;
    private String tweetMessage;
    private String userId;
    private String username;
    private String repliedTo;
    private List<String> listOfReplies;
    private List<String> listOfTags;
    private LocalDateTime tweetedAt;
}
