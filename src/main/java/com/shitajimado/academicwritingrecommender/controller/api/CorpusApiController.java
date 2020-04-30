package com.shitajimado.academicwritingrecommender.controller.api;

import com.shitajimado.academicwritingrecommender.entities.Corpus;
import com.shitajimado.academicwritingrecommender.entities.CorpusRepository;
import com.shitajimado.academicwritingrecommender.entities.Document;
import com.shitajimado.academicwritingrecommender.entities.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "api", consumes = "application/json", produces = "application/json")
public class CorpusApiController {
    @Autowired private CorpusRepository corpusRepository;
    @Autowired private DocumentRepository documentRepository;

    @PostMapping("/create_corpus")
    public void createCorpus(@RequestBody Corpus corpus) {
        corpusRepository.save(corpus);
    }

    @GetMapping(value = "/read_corpus", consumes = "application/x-www-form-urlencoded")
    public List<Corpus> readCorpus() {
        return corpusRepository.findAll();
    }

    @PostMapping("/update_corpus")
    public void updateCorpus(Model model) {

    }

    @PostMapping("/delete_corpus")
    public void deleteCorpus(@RequestBody Corpus corpus) {
        var docs = documentRepository.findAllById(corpus.getDocuments());
        documentRepository.deleteAll(docs);
        corpusRepository.delete(corpus);
    }
}
