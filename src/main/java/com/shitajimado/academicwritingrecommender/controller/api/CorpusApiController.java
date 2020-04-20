package com.shitajimado.academicwritingrecommender.controller.api;

import com.shitajimado.academicwritingrecommender.entities.Corpus;
import com.shitajimado.academicwritingrecommender.entities.CorpusRepository;
import com.shitajimado.academicwritingrecommender.entities.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class CorpusApiController {
    @Autowired private CorpusRepository repository;

    @PostMapping("/create_corpus")
    public void createCorpus(Model model) {
        var name = (String)model.getAttribute("name");
        var corpus = new Corpus(name, new HashSet<Document>());
        repository.save(corpus);
    }

    @GetMapping("/read_corpus")
    public Optional<Corpus> readCorpus(@RequestParam Long id) {
        var corpora = repository.findAll();
        return repository.findById(id);
    }

    @PostMapping("update_corpus")
    public void updateCorpus(Model model) {
        
    }

    @PostMapping("/delete_corpus")
    public void deleteCorpus(Model model) {
        repository.deleteById((Long)model.getAttribute("id"));
    }
}
