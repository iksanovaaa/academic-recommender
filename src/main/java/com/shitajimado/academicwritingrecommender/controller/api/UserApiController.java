package com.shitajimado.academicwritingrecommender.controller.api;

import com.shitajimado.academicwritingrecommender.entities.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api", consumes = "application/json", produces = "application/json")
public class UserApiController {
    @PostMapping(value = "/create_user")
    public void createUser(@RequestBody User user) {

    }
}
