package com.shitajimado.academicwritingrecommender.controller;

import com.google.common.collect.Iterables;
import com.shitajimado.academicwritingrecommender.core.TextWithNodes;
import com.shitajimado.academicwritingrecommender.entities.CorpusRepository;
import com.shitajimado.academicwritingrecommender.services.GateService;
import gate.Factory;
import gate.util.GateException;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class IndexController {
    @Autowired private GateService gateService;

    @Autowired private CorpusRepository corpusRepository;

    @GetMapping(value="/index")
    public String index(@RequestParam String filename, Model model) throws IOException, GateException, SAXException, ParserConfigurationException {
        model.addAttribute("name", filename);

        // ToDo: подфиксить обработку ошибок
        var document = Factory.newDocument(new URL("file:/C:/" + filename), "UTF-8");

        try {
            var featureMap = gateService.process(document);
            var text = TextWithNodes.fromXmlString(document.toXml());
            var html = text.toHtmlString();

            FileUtils.writeStringToFile(new File("C:/Users/Max/новый мерин.html"), html);

            model.addAttribute("colorizedHtml", html);
            model.addAttribute("annotations", text.getAnnotations());
        } finally {
            Factory.deleteResource(document);
        }

        return "index";
    }
}
