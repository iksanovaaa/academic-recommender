package com.shitajimado.academicwritingrecommender.controller.api;

import com.shitajimado.academicwritingrecommender.core.TextHelper;
import com.shitajimado.academicwritingrecommender.core.exceptions.TextNotPresentException;
import com.shitajimado.academicwritingrecommender.entities.*;
import com.shitajimado.academicwritingrecommender.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(value = "api", consumes = "application/json", produces = "application/json")
public class TextApiController {
    //@Autowired
    //private DocumentService documentService;

    /*@GetMapping(value = "/read_text", consumes = "application/x-www-form-urlencoded")
    public TextHelper readText(Document document) throws TextNotPresentException {
        return new TextHelper(documentService.readText(document));
    }

     */

    @GetMapping(value = "/read_text", consumes = "application/x-www-form-urlencoded")
    public TextHelper readText(Document document) throws TextNotPresentException {
        return null;
    }
}
