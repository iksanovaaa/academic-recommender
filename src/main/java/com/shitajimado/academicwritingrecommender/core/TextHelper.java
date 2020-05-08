package com.shitajimado.academicwritingrecommender.core;

import com.shitajimado.academicwritingrecommender.entities.Text;

import java.util.List;

public class TextHelper {
    private String text;
    private List<Annotation> annotations;
    private List<Annotation> annotationList;

    public TextHelper(Text text) {
        this.text = text.toHtmlString();
        this.annotations = text.getAnnotations();
        this.annotationList = text.getAnnotationList();
    }

    public String getText() {
        return text;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public List<Annotation> getAnnotationList() {
        return annotationList;
    }
}
