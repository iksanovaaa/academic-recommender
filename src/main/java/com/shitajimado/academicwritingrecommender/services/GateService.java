package com.shitajimado.academicwritingrecommender.services;

import gate.*;
import gate.util.GateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class GateService {
    @Autowired
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

    public FeatureMap process(Document doc) throws GateException {
        try {
            corpus.add(doc);
            application.execute();
            return doc.getFeatures();
        } finally {
            corpus.clear();
        }
    }
}