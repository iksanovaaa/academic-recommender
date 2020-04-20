package com.shitajimado.academicwritingrecommender.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DocumentRepository extends CrudRepository<Document, Long> {
    List<Document> findByName(String name);
}
