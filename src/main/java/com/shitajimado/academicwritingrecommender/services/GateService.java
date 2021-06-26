package com.shitajimado.academicwritingrecommender.services;

import com.shitajimado.academicwritingrecommender.core.AnnotationHandler;
import com.shitajimado.academicwritingrecommender.core.TextHandler;
import com.shitajimado.academicwritingrecommender.core.exceptions.DocumentNotCreatedException;
import com.shitajimado.academicwritingrecommender.entities.Text;
import gate.*;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;

@Service
public class GateService {
    /*@Autowired
    private CorpusController application;

    private Corpus corpus;

    @PostConstruct
    public void init() throws GateException {
        corpus = Factory.newCorpus("AcademicWriting");
        application.setCorpus(corpus);
    }

    @PreDestroy
    public void destroy() {
        Factory.deleteResource(corpus);
        Factory.deleteResource(application);
    }

    private FeatureMap process(Document doc) throws GateException {
        try {
            corpus.add(doc);
            application.execute();
            return doc.getFeatures();
        } finally {
            corpus.clear();
        }
    }

    private String parseWithGate(String content) throws DocumentNotCreatedException {
        gate.Document gateDoc = null; // Factory.newDocument(new URL("file:/C:/" + filename), "UTF-8");

        try {
            gateDoc = Factory.newDocument(content);
            process(gateDoc);
            return gateDoc.toXml();
        } catch (ResourceInstantiationException e) {
            throw new DocumentNotCreatedException("Unable to initialize the underlying GATE Document");
        } catch (GateException e) {
            throw new DocumentNotCreatedException("Error occurred while processing with GATE");
        } finally {
            if (gateDoc != null) {
                Factory.deleteResource(gateDoc);
            }
        }
    }

    public Text processWithGate(String content) throws DocumentNotCreatedException {
        try {
            var xml = parseWithGate(content);
            var parser = SAXParserFactory.newInstance().newSAXParser();
            var reader = parser.getXMLReader();

            var handler = new TextHandler();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xml)));

            var textWithNodes = handler.extractTextWithNodes();

            var annotationHandler = new AnnotationHandler();
            reader.setContentHandler(annotationHandler);
            reader.parse(new InputSource(new StringReader(xml)));

            var annotations = annotationHandler.extractAnnotations();

            for (var annotation : annotations) {
                textWithNodes.addAnnotation(annotation);
            }

            return textWithNodes;
        } catch (ParserConfigurationException e) {
            throw new DocumentNotCreatedException("Error while configurating the parser");
        } catch (IOException e) {
            throw new DocumentNotCreatedException("Error while performing I/O operations");
        } catch (SAXException e) {
            throw new DocumentNotCreatedException("Internal XML parser exception");
        }
    }

     */


    @PostConstruct
    public void init() throws GateException {

    }

    @PreDestroy
    public void destroy() {

    }

    private FeatureMap process(Document doc) throws GateException {
        return null;
    }

    private String parseWithGate(String content) throws DocumentNotCreatedException {
        gate.Document gateDoc = null; // Factory.newDocument(new URL("file:/C:/" + filename), "UTF-8");

        try {
            gateDoc = Factory.newDocument(content);
            process(gateDoc);
            return gateDoc.toXml();
        } catch (ResourceInstantiationException e) {
            throw new DocumentNotCreatedException("Unable to initialize the underlying GATE Document");
        } catch (GateException e) {
            throw new DocumentNotCreatedException("Error occurred while processing with GATE");
        } finally {
            if (gateDoc != null) {
                Factory.deleteResource(gateDoc);
            }
        }
    }

    public Text processWithGate(String content) throws DocumentNotCreatedException {
        try {
            var xml = parseWithGate(content);
            var parser = SAXParserFactory.newInstance().newSAXParser();
            var reader = parser.getXMLReader();

            var handler = new TextHandler();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xml)));

            var textWithNodes = handler.extractTextWithNodes();

            var annotationHandler = new AnnotationHandler();
            reader.setContentHandler(annotationHandler);
            reader.parse(new InputSource(new StringReader(xml)));

            var annotations = annotationHandler.extractAnnotations();

            for (var annotation : annotations) {
                textWithNodes.addAnnotation(annotation);
            }

            return textWithNodes;
        } catch (ParserConfigurationException e) {
            throw new DocumentNotCreatedException("Error while configurating the parser");
        } catch (IOException e) {
            throw new DocumentNotCreatedException("Error while performing I/O operations");
        } catch (SAXException e) {
            throw new DocumentNotCreatedException("Internal XML parser exception");
        }
    }
}