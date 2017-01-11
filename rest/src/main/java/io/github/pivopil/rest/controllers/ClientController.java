package io.github.pivopil.rest.controllers;

import io.github.pivopil.rest.constants.REST_API;
import io.github.pivopil.rest.services.CustomClientDetailsService;
import io.github.pivopil.share.entities.impl.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 08.01.17.
 */

@RestController
@RequestMapping(REST_API.CLIENTS)
public class ClientController {

    private final CustomClientDetailsService customClientDetailsService;

    @Autowired
    public ClientController(CustomClientDetailsService customClientDetailsService) {
        this.customClientDetailsService = customClientDetailsService;
    }

    @GetMapping
    public Iterable<Client> list() {
        return customClientDetailsService.list();
    }

    @GetMapping(REST_API.ID_PATH_VARIABLE)
    public Client getSingle(@PathVariable("id") Long id) {
        return customClientDetailsService.getSingle(id);
    }

    @PostMapping
    public Client create(@RequestBody Client client) {
        return customClientDetailsService.save(client);
    }

    @PutMapping(REST_API.ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Client client, @PathVariable("id") Long id) {
        client.setId(id);
        customClientDetailsService.update(client);
    }

    @DeleteMapping(REST_API.ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        customClientDetailsService.delete(id);
    }


}
