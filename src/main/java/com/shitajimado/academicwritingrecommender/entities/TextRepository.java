package com.shitajimado.academicwritingrecommender.entities;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TextRepository extends MongoRepository<Text, String> {

}
