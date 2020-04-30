package com.shitajimado.academicwritingrecommender.core;

import com.shitajimado.academicwritingrecommender.entities.Text;

import java.util.List;

public class TextHelper {
    private String text;
    private List<Annotation> annotations;

    public TextHelper(Text text) {
        this.text = text.toHtmlString();
        this.annotations = text.getAnnotations();
    }

    public String getText() {
        return text;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }
}
