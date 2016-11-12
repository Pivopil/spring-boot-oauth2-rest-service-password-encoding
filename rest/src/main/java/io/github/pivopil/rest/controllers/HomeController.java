package io.github.pivopil.rest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created on 03.06.16.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "index.html";
    }


    /*
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
    */

}
