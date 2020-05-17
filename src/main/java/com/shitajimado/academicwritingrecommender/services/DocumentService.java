package com.shitajimado.academicwritingrecommender.services;

import com.google.common.collect.Lists;
import com.shitajimado.academicwritingrecommender.entities.dtos.DocumentDto;
import com.shitajimado.academicwritingrecommender.entities.dtos.TextDto;
import com.shitajimado.academicwritingrecommender.core.exceptions.DocumentNotCreatedException;
import com.shitajimado.academicwritingrecommender.core.exceptions.TextNotPresentException;
import com.shitajimado.academicwritingrecommender.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private TextService textService;

    @Transactional
    Document createDocument(DocumentDto documentDto) throws DocumentNotCreatedException {
        var text = textService.createText(new TextDto(documentDto.getContent()));

        var document = new Document(documentDto);
        document.setTextId(text.getId());
        return documentRepository.save(document);
    }

    @Transactional
    List<Document> readDocuments(Set<String> ids) {
        return Lists.newArrayList(documentRepository.findAllById(ids));
    }

    @Transactional
    void deleteDocument(Document document) {
        textService.deleteText(document.getTextId());
        documentRepository.delete(document);
    }

    @Transactional
    void deleteDocuments(Set<String> ids) {
        var docs = documentRepository.findAllById(ids);
        documentRepository.deleteAll(docs);
    }

    @Transactional
    public Text readText(Document document) throws TextNotPresentException {
        return textService.readText(document.getTextId());
    }
}
