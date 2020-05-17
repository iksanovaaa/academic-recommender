package com.shitajimado.academicwritingrecommender.controller.api;

import com.shitajimado.academicwritingrecommender.core.exceptions.UserAlreadyExistsException;
import com.shitajimado.academicwritingrecommender.core.exceptions.UserNotExistsException;
import com.shitajimado.academicwritingrecommender.entities.User;
import com.shitajimado.academicwritingrecommender.entities.dtos.UserDto;
import com.shitajimado.academicwritingrecommender.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api", consumes = "application/x-www-form-urlencoded", produces = "application/json")
public class UserApiController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    public User register(@ModelAttribute UserDto userDto) throws UserAlreadyExistsException {
        return userService.register(userDto);
    }

    @PostMapping(value = "/login")
    public User login(@ModelAttribute UserDto userDto) throws UserNotExistsException {
        return userService.login(userDto);
    }
}
