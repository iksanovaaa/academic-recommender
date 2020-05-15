package com.shitajimado.academicwritingrecommender.services;

import com.google.common.collect.Sets;
import com.shitajimado.academicwritingrecommender.core.dtos.CorpusDto;
import com.shitajimado.academicwritingrecommender.core.dtos.DocumentDto;
import com.shitajimado.academicwritingrecommender.core.exceptions.CorpusNotFoundException;
import com.shitajimado.academicwritingrecommender.core.exceptions.DocumentNotCreatedException;
import com.shitajimado.academicwritingrecommender.entities.Corpus;
import com.shitajimado.academicwritingrecommender.entities.CorpusRepository;
import com.shitajimado.academicwritingrecommender.entities.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class CorpusService {
    @Autowired
    private CorpusRepository corpusRepository;
    @Autowired
    private DocumentService documentService;

    @Transactional
    public Corpus createCorpus(CorpusDto corpusDto) {
        var corpus = new Corpus(corpusDto);
        return corpusRepository.save(corpus);
    }

    @Transactional
    public List<Corpus> readCorpora() {
        return corpusRepository.findAll();
    }

    @Transactional
    public void deleteCorpus(Corpus corpus) {
        documentService.deleteDocuments(corpus.getDocuments());
        corpusRepository.delete(corpus);
    }

    @Transactional
    public Corpus createAndAddDocument(DocumentDto documentDto) throws DocumentNotCreatedException {
        var document = documentService.createDocument(documentDto);

        return corpusRepository.findById(document.getCorpusId()).map(
                corpus -> corpus.addDocument(document)
        ).map(
                corpus -> corpusRepository.save(corpus)
        ).orElseThrow(() -> new DocumentNotCreatedException("Unable to create a document"));
    }

    @Transactional
    public List<Corpus> createAndAddDocuments(List<DocumentDto> documentDtos) throws DocumentNotCreatedException {
        List<Corpus> corpora = new ArrayList<>();

        for (var documentDto : documentDtos) {
            var corpus = createAndAddDocument(documentDto);
            corpora.add(corpus);
        }

        return corpora;
    }

    @Transactional
    public List<Document> readDocuments(Corpus corpus) {
        return documentService.readDocuments(corpus.getDocuments());
    }

    @Transactional
    public List<Document> readDocuments(String corpusId) throws CorpusNotFoundException {
        return corpusRepository.findById(corpusId).map(
                corpus -> documentService.readDocuments(corpus.getDocuments())
        ).orElseThrow(
                () -> new CorpusNotFoundException("Unable to find a corpus with the given ID")
        );
    }

    @Transactional
    public Corpus removeDocument(Document document) throws CorpusNotFoundException {
        return corpusRepository.findById(document.getCorpusId()).map(
                corpus -> {
                    corpus.removeDocument(document);
                    documentService.deleteDocument(document);
                    return corpusRepository.save(corpus);
                }
        ).orElseThrow(() -> new CorpusNotFoundException("Unable to find a corpus"));
    }
}
