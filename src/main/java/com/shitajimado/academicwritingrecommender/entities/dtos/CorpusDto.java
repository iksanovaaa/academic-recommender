package com.shitajimado.academicwritingrecommender.entities.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CorpusDto {
    private String name;

    @JsonCreator
    public CorpusDto(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
