package com.shitajimado.academicwritingrecommender.controller;

import gate.util.GateException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Controller
public class IndexController {
    @GetMapping(value="/index")
    public String index(Model model) throws IOException, GateException, SAXException, ParserConfigurationException {
        //model.addAttribute("name", filename);

        return "index";
    }
}
