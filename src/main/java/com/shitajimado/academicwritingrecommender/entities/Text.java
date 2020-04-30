package com.shitajimado.academicwritingrecommender.entities;

import com.shitajimado.academicwritingrecommender.core.Annotation;
import com.shitajimado.academicwritingrecommender.core.TextNode;
import org.springframework.data.annotation.Id;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class Text {
    @Id
    private String id;
    private List<TextNode> nodes;
    private Map<Long, Long> idMapping;
    private Set<String> annotationsSet = new HashSet<>();
    private List<Annotation> annotations = new ArrayList<>();

    public Text() {

    }

    public Text(Collection<TextNode> nodes) {
        this.nodes = new ArrayList<>(nodes);
        this.idMapping = new HashMap<>();

        for (int i = 0; i < this.nodes.size(); ++i) {
            idMapping.put(this.nodes.get(i).getId(), (long) i);
        }
    }

    public List<TextNode> getNodes() {
        return nodes;
    }

    public void addAnnotation(Annotation annotation) {
        var type = annotation.getName();

        if (!annotationsSet.contains(type)) {
            annotationsSet.add(type);
            annotations.add(annotation);
        }

        var begin = annotation.getStartNode();
        var end = annotation.getEndNode();

        int idx = Math.toIntExact(idMapping.get(begin));

        while (begin < end) {
            if (this.nodes.get(idx).getId().equals(begin)) {
                this.nodes.get(idx).addAnnotation(annotation);
                ++idx;
            }

            ++begin;
        }
    }

    public String toHtmlString() {
        var sb = new StringBuilder();

        sb.append("<div class=\"colorized-text\">");

        for (var node : nodes) {
            node.appendAsHtml(sb);
        }

        sb.append("</div>");

        return sb.toString();
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public String getId() {
        return id;
    }
}
