package com.shitajimado.academicwritingrecommender.controller.api;

import com.shitajimado.academicwritingrecommender.entities.dtos.CorpusDto;
import com.shitajimado.academicwritingrecommender.entities.Corpus;
import com.shitajimado.academicwritingrecommender.services.CorpusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api", consumes = "application/json", produces = "application/json")
public class CorpusApiController {
    //@Autowired
    //private CorpusService corpusService;

    /*@PostMapping("/create_corpus")
    public Corpus createCorpus(@RequestBody CorpusDto corpusDto) {
        return corpusService.createCorpus(corpusDto);
    }

    @GetMapping(value = "/read_corpus", consumes = "application/x-www-form-urlencoded")
    public List<Corpus> readCorpus() {
        return corpusService.readCorpora();
    }

    @PostMapping("/update_corpus")
    public void updateCorpus(Model model) {

    }

    @PostMapping("/delete_corpus")
    public void deleteCorpus(@RequestBody Corpus corpus) {
        corpusService.deleteCorpus(corpus);
    }

     */

    @PostMapping("/create_corpus")
    public Corpus createCorpus(@RequestBody CorpusDto corpusDto) {
        return null;
    }

    @GetMapping(value = "/read_corpus", consumes = "application/x-www-form-urlencoded")
    public List<Corpus> readCorpus() {
        return null;
    }

    @PostMapping("/update_corpus")
    public void updateCorpus(Model model) {

    }

    @PostMapping("/delete_corpus")
    public void deleteCorpus(@RequestBody Corpus corpus) {
        ;
    }
}