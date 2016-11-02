package io.github.pivopil.rest.controllers;

import io.github.pivopil.REST_API;
import io.github.pivopil.rest.services.ApiService;
import io.github.pivopil.rest.services.CustomUserDetailsService;
import io.github.pivopil.rest.services.WebSocketService;
import io.github.pivopil.share.entities.impl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created on 05.07.16.
 */

@RestController
public class ApiController {

    @Autowired
    private ApiService apiService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @RequestMapping(REST_API.API)
    public Map<String, Map<String, String>> getApi() {
        return apiService.getApi();
    }


    // ws test
    @RequestMapping("/wstest")
    public void wsTest() {
        webSocketService.broadcastCurrentData("Super test");
    }

    // acl test
    @RequestMapping("/api/acl")
    @PostAuthorize("hasPermission(returnObject,'ADMIN')")
    public Iterable<User> acl() {
        return customUserDetailsService.findAll();
    }

    // @PreAuthorize('hasPermission(#object,read)')
    @RequestMapping("/api/acl2/{id}")
    @PreAuthorize("@customUserDetailsService.canAccessUser(principal, #id)")
    public Iterable<User> acl2(@PathVariable Long id) {
        return customUserDetailsService.findAll();
    }

}
