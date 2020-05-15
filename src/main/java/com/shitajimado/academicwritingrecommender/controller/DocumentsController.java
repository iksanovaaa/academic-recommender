package com.shitajimado.academicwritingrecommender.controller;

import com.shitajimado.academicwritingrecommender.entities.Corpus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DocumentsController {
    @GetMapping(value = "/documents")
    public String documents(Model model, Corpus corpus) {
        model.addAttribute("corpus", corpus);
        return "documents";
    }
}
