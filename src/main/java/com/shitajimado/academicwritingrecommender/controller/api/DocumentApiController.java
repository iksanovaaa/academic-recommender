package com.shitajimado.academicwritingrecommender.controller.api;

import com.shitajimado.academicwritingrecommender.entities.Corpus;
import com.shitajimado.academicwritingrecommender.entities.Document;
import com.shitajimado.academicwritingrecommender.entities.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "api", consumes = "application/json", produces = "application/json")
public class DocumentApiController {
    @Autowired private DocumentRepository repository;

    @PostMapping("/create_document")
    public void createDocument(@RequestBody Document document) {
        //var corpus = new Corpus((String)model.getAttribute("name"), new HashSet<>());
        repository.save(document);
    }

    @GetMapping("/read_document")
    public List<Document> readDocument() {
        return repository.findAll();
    }

    @PostMapping("/update_document")
    public void updateDocument(Model model) {

    }

    @PostMapping("/delete_document")
    public void deleteDocument(Model model) {
        repository.deleteById((String) Objects.requireNonNull(model.getAttribute("id")));
    }
}

