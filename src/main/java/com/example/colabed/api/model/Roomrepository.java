package com.example.colabed.api.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface Roomrepository extends MongoRepository<Room,String> {
//    @Query("{'roomCode': ?0}")
//    Room findRoomByRoomCode(String roomCode);

    @Query("{'roomName': ?0}")
    Room findRoomByRoomName(String roomName);


}
