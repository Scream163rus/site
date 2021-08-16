package com.scream.site.controllers;

import com.scream.site.models.User;
import com.scream.site.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private UserService userService;
    RegistrationController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user) {
        userService.saveUser(user);
        return "redirect:/login";
    }
}