package com.shitajimado.academicwritingrecommender.services;

import com.shitajimado.academicwritingrecommender.entities.dtos.TextDto;
import com.shitajimado.academicwritingrecommender.core.exceptions.DocumentNotCreatedException;
import com.shitajimado.academicwritingrecommender.core.exceptions.TextNotPresentException;
import com.shitajimado.academicwritingrecommender.entities.Text;
import com.shitajimado.academicwritingrecommender.entities.TextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TextService {
    @Autowired
    private GateService gateService;
    @Autowired
    private TextRepository textRepository;

    @Transactional()
    Text createText(TextDto textDto) throws DocumentNotCreatedException {
        var text = gateService.processWithGate(textDto.getContent());
        text = textRepository.save(text);
        return text;
    }

    @Transactional
    Text readText(String id) throws TextNotPresentException {
        return textRepository.findById(id).orElseThrow(() -> new TextNotPresentException("Unable to find the text"));
    }

    @Transactional
    void deleteText(String id) {
        textRepository.deleteById(id);
    }
}
