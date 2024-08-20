package com.takehome.coderhack.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.takehome.coderhack.entities.User;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllByOrderByScoreDesc();
}
