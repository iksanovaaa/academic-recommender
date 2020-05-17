package com.shitajimado.academicwritingrecommender.entities;

import com.shitajimado.academicwritingrecommender.entities.dtos.DocumentDto;
import com.shitajimado.academicwritingrecommender.core.exceptions.DocumentNotCreatedException;
import org.springframework.data.annotation.Id;

public class Document {
    @Id
    private String id;
    private String corpusId;
    private String name;
    private String textId;

    public Document() {

    }

    public Document(String corpusId, String name) {
        this.corpusId = corpusId;
        this.name = name;
    }

    public Document(String name, String content, Text annotatedText) throws DocumentNotCreatedException {
        this.corpusId = "";
        this.name = name;
        this.textId = annotatedText.getId();
    }

    public Document(DocumentDto documentDto) {
        this(documentDto.getCorpusId(), documentDto.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getCorpusId() {
        return corpusId;
    }

    public void setCorpusId(String corpusId) {
        this.corpusId = corpusId;
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }
}
