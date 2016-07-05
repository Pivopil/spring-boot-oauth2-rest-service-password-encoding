package io.github.pivopil.rest.controllers;

import io.github.pivopil.REST_API;
import io.github.pivopil.rest.services.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * Created on 05.07.16.
 */

@RestController
public class ApiController {

    @Autowired
    private ApiService apiService;

    @RequestMapping(REST_API.API)
    public Map<String, Map<String, Set<String>>> getApi() {
        return apiService.getApi();
    }
}
