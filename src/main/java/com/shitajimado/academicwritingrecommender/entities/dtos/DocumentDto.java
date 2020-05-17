package com.shitajimado.academicwritingrecommender.entities.dtos;

public class DocumentDto {
    private String corpusId;
    private String name;
    private String content;

    public DocumentDto(String corpusId, String name, String content) {
        this.corpusId = corpusId;
        this.name = name;
        this.content = content;
    }

    public String getCorpusId() {
        return corpusId;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
