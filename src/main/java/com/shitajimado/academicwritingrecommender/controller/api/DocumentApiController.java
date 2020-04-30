package com.shitajimado.academicwritingrecommender.controller.api;

import com.shitajimado.academicwritingrecommender.core.exceptions.DocumentNotCreatedException;
import com.shitajimado.academicwritingrecommender.entities.*;
import com.shitajimado.academicwritingrecommender.services.GateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "api", consumes = "application/json", produces = "application/json")
public class DocumentApiController {
    @Autowired private GateService gateService;
    @Autowired private CorpusRepository corpusRepository;
    @Autowired private DocumentRepository documentRepository;
    @Autowired private TextRepository textRepository;

    @PostMapping("/create_document")
    public void createDocument(@RequestBody Document document) throws DocumentNotCreatedException {
        var text = gateService.processWithGate(document.getContent());
        textRepository.save(text);

        document.setAnnotatedText(text);
        var saved = documentRepository.save(document);

        corpusRepository.findById(document.getCorpusId()).map(
                corpus -> {
                    corpus.addDocument(saved);
                    return corpusRepository.save(corpus);
                }
        ).orElseThrow(() -> new DocumentNotCreatedException("Unable to create a document"));
    }

    @GetMapping(value = "/read_document", consumes = "application/x-www-form-urlencoded")
    public Set<Document> readDocument(@RequestParam String corpus) {
        return corpusRepository.findById(corpus)
                .map(Corpus::getDocuments)
                .orElseGet(HashSet::new);
    }

    @PostMapping("/update_document")
    public void updateDocument(Model model) {

    }

    @PostMapping("/delete_document")
    public void deleteDocument(@RequestBody Document document) {
        corpusRepository.findById(document.getCorpusId()).ifPresent(
                corpus -> {
                    corpus.getDocuments().remove(document);
                    documentRepository.delete(document);
                    corpusRepository.save(corpus);
                }
        );
    }

    @GetMapping("/refresh")
    public void refresh() throws DocumentNotCreatedException {
        textRepository.deleteAll();
        documentRepository.deleteAll();
        corpusRepository.deleteAll();

        var content = "Hi, I'm a native speaker. I will try to do my best";
        var text = textRepository.save(gateService.processWithGate(content));

        var corpus = corpusRepository.save(new Corpus("Teacher's", new HashSet<>()));

        var document = documentRepository.save(new Document(corpus.getId(), "DRIPSET", content, text));

        corpus.addDocument(document);
        corpusRepository.save(corpus);
    }
}

