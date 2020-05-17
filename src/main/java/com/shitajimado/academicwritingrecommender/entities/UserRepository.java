package com.shitajimado.academicwritingrecommender.entities;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
}