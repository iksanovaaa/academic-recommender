package com.shitajimado.academicwritingrecommender.controller.api;

import com.shitajimado.academicwritingrecommender.core.Statistics;
import com.shitajimado.academicwritingrecommender.core.exceptions.CorpusNotFoundException;
import com.shitajimado.academicwritingrecommender.entities.CorpusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api", consumes = "application/json", produces = "application/json")
public class StatisticsApiController {
    @Autowired
    private CorpusRepository corpusRepository;

    @GetMapping(value = "read_statistics", consumes = "application/x-www-form-urlencoded")
    public Statistics readStatistics(@RequestParam String corpusId) throws CorpusNotFoundException {
        return corpusRepository.findById(corpusId).map(
                corpus -> new Statistics("", 0L)
        ).orElseThrow(() -> new CorpusNotFoundException("Unable to find a corpus with given ID"));
    }
}
