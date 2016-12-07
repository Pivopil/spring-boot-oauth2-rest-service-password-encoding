package io.github.pivopil.rest.controllers;

import io.github.pivopil.rest.constants.REST_API;
import io.github.pivopil.rest.services.CustomUserDetailsService;
import io.github.pivopil.share.entities.impl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public UserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping(REST_API.ME)
    public UserDetails me(@AuthenticationPrincipal User user) {
        return customUserDetailsService.loadUserByUsername(user.getLogin());
    }

    @GetMapping(REST_API.USERS)
    public Iterable<User> getUsers() {
        return customUserDetailsService.findAll();
    }

}
