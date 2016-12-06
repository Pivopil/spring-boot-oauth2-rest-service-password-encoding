package io.github.pivopil.rest.controllers;

import io.github.pivopil.REST_API;
import io.github.pivopil.rest.services.CustomUserDetailsService;
import io.github.pivopil.share.entities.impl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @RequestMapping(REST_API.ME)
    public UserDetails me(@AuthenticationPrincipal User user) {
        return customUserDetailsService.loadUserByUsername(user.getLogin());
    }

    @RequestMapping(REST_API.USERS)
    public Iterable<User> getUsers() {
        return customUserDetailsService.findAll();
    }

}
