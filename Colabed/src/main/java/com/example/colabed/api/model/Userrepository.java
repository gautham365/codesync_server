package com.example.colabed.api.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Userrepository extends MongoRepository<User,String> {
    @Query("{'accessToken': ?0}")
    User findUserByToken(String token);

    @Query("{'email': ?0}")
    User findUserByEmailId(String email);

}
