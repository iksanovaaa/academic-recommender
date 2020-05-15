package com.shitajimado.academicwritingrecommender.core;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

public class AnnotationHandler extends DefaultHandler {
    private static final Set<String> banWords = new HashSet<>(
            Arrays.asList("sentence", "spacetoken", "split", "token", "paragraph"));

    private List<Annotation> nodes = new ArrayList<>();
    private boolean working = false;
    private Map<String, String> nameMapping = new HashMap<>();

    public List<Annotation> extractAnnotations() {
        nodes.sort(Comparator.comparing(Annotation::getStartNode));
        return nodes;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        var lowerName = qName.toLowerCase();

        if (working && lowerName.equals("annotation")) {
            /* var id = Long.parseLong(attributes.getValue(uri, "id"));
            var type = attributes.getValue(uri, "token");
            var start = Long.parseLong(attributes.getValue(uri, "startnode"));
            var end = Long.parseLong(attributes.getValue(uri, "endnode")); */
            // ToDo: find by name
            var id = Long.parseLong(attributes.getValue(0));
            var type = attributes.getValue(1);
            var start = Long.parseLong(attributes.getValue(2));
            var end = Long.parseLong(attributes.getValue(3));

            if (filterByType(type)) {
                return;
            }

            if (!nameMapping.containsKey(type)) {
                nameMapping.put(type, String.format("color-%d", nameMapping.size() + 1));
            }

            nodes.add(new Annotation(id, type, nameMapping.get(type), start, end));
        } else if (lowerName.equals("annotationset")) {
            working = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (qName.toLowerCase().equals("annotationset")) {
            working = false;
        }
    }

    private static boolean filterByType(String type) {
        return banWords.contains(type.toLowerCase());
    }
}
