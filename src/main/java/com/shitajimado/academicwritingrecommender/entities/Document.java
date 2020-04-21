package com.shitajimado.academicwritingrecommender.entities;

import org.springframework.data.annotation.Id;

import java.net.URL;

public class Document {
    @Id
    private String id;
    private String name;
    private String content;
    private String htmlContent;

    public Document() {}

    public Document(String name, URL url, String content, String htmlContent) {
        this.name = name;
        this.content = content;
        this.htmlContent = htmlContent;
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

    public void setContent(String content) {
        // ToDo: run transform to HTML
        this.content = content;
    }

    public String getHtmlContent() {
        return htmlContent;
    }
}
