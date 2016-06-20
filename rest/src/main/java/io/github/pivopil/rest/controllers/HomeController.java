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
    
}
