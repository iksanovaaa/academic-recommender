package com.shitajimado.academicwritingrecommender.entities;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CorpusRepository extends MongoRepository<Corpus, String> {
    List<Corpus> findByName(String name);
}
