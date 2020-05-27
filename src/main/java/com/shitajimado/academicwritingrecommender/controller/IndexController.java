package com.shitajimado.academicwritingrecommender.controller;

import com.shitajimado.academicwritingrecommender.core.exceptions.UserAlreadyExistsException;
import com.shitajimado.academicwritingrecommender.core.exceptions.UserNotExistsException;
import com.shitajimado.academicwritingrecommender.entities.dtos.UserDto;
import com.shitajimado.academicwritingrecommender.services.UserService;
import gate.util.GateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Controller
public class IndexController {
    @Autowired
    UserService userService;

    @GetMapping(value="/index")
    public String index(Model model) {
        //model.addAttribute("name", filename);

        return "index";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "authentication";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "registration";
    }

    /* @PostMapping("/login")
    public String login(@ModelAttribute UserDto userDto) throws UserNotExistsException {
        userService.login(userDto);
        return "index";
    } */

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto userDto) throws UserAlreadyExistsException, UserNotExistsException {
        userService.register(userDto);
        return "index";
    }
}
