package com.shitajimado.academicwritingrecommender.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextNode {
    private Long id;
    private String text;
    private List<String> annotations;

    public TextNode(Long id, String text) {
        this.id = id;
        this.text = text;
        this.annotations= new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getAnnotations() {
        return Collections.unmodifiableList(annotations);
    }

    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation.getType());
    }

    public void appendAsHtml(StringBuilder sb) {
        sb.append("<div class=\"colorized-text-node");

        for (var annotation : annotations) {
            sb.append(" ");
            sb.append(annotation.toLowerCase());
        }

        sb.append("\">");
        sb.append(text);
        sb.append("</div>");
    }
}
