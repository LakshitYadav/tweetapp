package com.tweetapp.repository;

import com.tweetapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Lakshit Yadav
 */

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findUserByEmail(String email);
}
