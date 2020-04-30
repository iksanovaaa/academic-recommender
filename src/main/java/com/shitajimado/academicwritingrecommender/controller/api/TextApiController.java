package com.shitajimado.academicwritingrecommender.controller.api;

import com.shitajimado.academicwritingrecommender.core.TextHelper;
import com.shitajimado.academicwritingrecommender.core.exceptions.TextNotPresentException;
import com.shitajimado.academicwritingrecommender.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(value = "api", consumes = "application/json", produces = "application/json")
public class TextApiController {
    @Autowired private DocumentRepository documentRepository;
    @Autowired private TextRepository textRepository;

    @GetMapping(value = "/read_text", consumes = "application/x-www-form-urlencoded")
    public TextHelper readText(@RequestParam String document) throws TextNotPresentException {
        return documentRepository.findById(document).flatMap(
                doc -> textRepository.findById(doc.getTextId()).map(TextHelper::new)
        ).orElseThrow(() -> new TextNotPresentException("Unable to find text"));
    }
}
