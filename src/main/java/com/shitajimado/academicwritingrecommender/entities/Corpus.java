package com.shitajimado.academicwritingrecommender.entities;

import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Corpus {
    @Id
    private String id;
    private String name;

    private Set<Document> documents = new HashSet<>();

    public Corpus() {}

    public Corpus(String name, Set<Document> documents) {
        this.name = name;
        this.documents = documents;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}
