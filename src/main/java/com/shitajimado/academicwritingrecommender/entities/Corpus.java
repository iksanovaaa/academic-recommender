package com.shitajimado.academicwritingrecommender.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Corpus {
    @Id
    private String id;
    private String name;
    private Set<String> documents = new HashSet<>();

    public Corpus() {}

    public Corpus(String name) {
        this.name = name;
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

    public Set<String> getDocuments() {
        return documents;
    }

    public Corpus addDocument(Document document) {
        this.documents.add(document.getId());
        document.setCorpusId(this.id);
        return this;
    }

    public Corpus removeDocument(Document document) {
        this.documents.remove(document.getId());
        return this;
    }
}
