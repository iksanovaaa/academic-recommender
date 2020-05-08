package com.shitajimado.academicwritingrecommender.controller.api;

import com.shitajimado.academicwritingrecommender.core.Statistics;
import com.shitajimado.academicwritingrecommender.core.StatisticsNode;
import com.shitajimado.academicwritingrecommender.core.exceptions.CorpusNotFoundException;
import com.shitajimado.academicwritingrecommender.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "api", consumes = "application/json", produces = "application/json")
public class StatisticsApiController {
    @Autowired private CorpusRepository corpusRepository;
    @Autowired private DocumentRepository documentRepository;
    @Autowired private TextRepository textRepository;

    @GetMapping(value = "read_statistics", consumes = "application/x-www-form-urlencoded")
    public Statistics readStatistics(@RequestParam String corpusId) throws CorpusNotFoundException {
        return corpusRepository.findById(corpusId).map(
                Corpus::getDocuments
        ).map(
                documentIds -> findByIds(documentIds.stream(), documentRepository)
        ).map(
                documentStream -> documentStream.map(Document::getTextId)
        ).map(
                textIdStream -> findByIds(textIdStream, textRepository)
        ).map(Statistics::fromTextStream).orElseThrow(
                () -> new CorpusNotFoundException("Unable to find a corpus with given ID")
        );
    }

    private <T> Stream<T> findByIds(Stream<String> stream, MongoRepository<T, String> repository) {
        return stream.map(item -> repository.findById(item).get());
    }
}
