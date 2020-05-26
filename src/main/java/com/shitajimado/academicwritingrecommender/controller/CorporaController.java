package com.shitajimado.academicwritingrecommender.controller;

import com.shitajimado.academicwritingrecommender.services.CorpusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CorporaController {
    @Autowired
    private CorpusService corpusService;

    @GetMapping(value = "/corpora")
    public ModelAndView corpora() {
        var mv = new ModelAndView("corpora");
        mv.addObject("corpora", corpusService.readCorpora());
        return mv;
    }
}
