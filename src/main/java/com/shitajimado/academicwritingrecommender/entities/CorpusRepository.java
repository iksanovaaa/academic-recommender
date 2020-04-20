package com.shitajimado.academicwritingrecommender.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CorpusRepository extends CrudRepository<Corpus, Long> {
    List<Corpus> findByName(String name);
}
