package com.shitajimado.academicwritingrecommender.entities;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentRepository extends MongoRepository<Document, String> {
    List<Document> findByName(String name);
}
