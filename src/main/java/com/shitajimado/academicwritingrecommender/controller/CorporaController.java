package com.shitajimado.academicwritingrecommender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CorporaController {
    @GetMapping(value = "/corpora")
    public String corpora() {
        return "corpora";
    }
}
