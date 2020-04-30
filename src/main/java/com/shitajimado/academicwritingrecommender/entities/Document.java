package com.shitajimado.academicwritingrecommender.entities;

import com.shitajimado.academicwritingrecommender.core.exceptions.DocumentNotCreatedException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.net.URL;

public class Document {
    @Id
    private String id;
    private String corpusId;
    private String name;
    private String content;
    @DBRef
    private Text annotatedText;

    public Document() {

    }

    public Document(String corpusId, String name, String content, Text annotatedText) throws DocumentNotCreatedException {
        this.corpusId = corpusId;
        this.name = name;
        this.setContent(content);
        this.annotatedText = annotatedText;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) throws DocumentNotCreatedException {
        this.content = content;
    }

    public Text getAnnotatedText() {
        return annotatedText;
    }

    public void setAnnotatedText(Text annotatedText) {
        this.annotatedText = annotatedText;
    }

    public String getCorpusId() {
        return corpusId;
    }

    public void setCorpusId(String corpusId) {
        this.corpusId = corpusId;
    }
}
