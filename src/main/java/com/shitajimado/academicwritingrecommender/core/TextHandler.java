package com.shitajimado.academicwritingrecommender.core;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.time.temporal.TemporalQueries;
import java.util.ArrayList;
import java.util.List;

public class TextWithNodeHandler extends DefaultHandler {
    private List<TextNode> nodes = new ArrayList<>();
    private Long currentId;

    private boolean working = false;

    public TextWithNodes extractTextWithNodes() {
        return new TextWithNodes(nodes);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (working) {
            // currentId = Long.parseLong(attributes.getValue(uri, "id"));
            // ToDo: find by name
            currentId = Long.parseLong(attributes.getValue(0));
        }

        if (qName.toLowerCase().equals("textwithnodes")) {
            working = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (qName.toLowerCase().equals("textwithnodes")) {
            working = false;

            if (!nodes.get(nodes.size() - 1).getId().equals(currentId)) {
                nodes.add(new TextNode(currentId, ""));
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        if (working) {
            var string = new String(ch, start, length);

            if (nodes.size() > 0 && nodes.get(nodes.size() - 1).getId().equals(currentId)) {
                var last = nodes.get(nodes.size() - 1);
                last.setText(last.getText() + string);
            } else {
                nodes.add(new TextNode(currentId, string));
            }
        }
    }
}
