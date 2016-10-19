package io.github.pivopil.rest.controllers.domain;

import io.github.pivopil.REST_API;
import io.github.pivopil.rest.services.domain.PersonalPostService;
import io.github.pivopil.rest.services.domain.PublicPostService;
import io.github.pivopil.share.entities.impl.domain.PersonalPost;
import io.github.pivopil.share.entities.impl.domain.PublicPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created on 19.10.16.
 */
@RestController
@RequestMapping(value = REST_API.PUBLIC_POST)
public class PublicPostController {

    @Autowired
    private PublicPostService publicPostService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Iterable<PublicPost>> list() {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(publicPostService.getAll(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public PublicPost create(@RequestBody PublicPost todo) {
        return publicPostService.add(todo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody PublicPost updatedTodo, @PathVariable("id") Long id) throws IOException {
        updatedTodo.setId(id);
        publicPostService.edit(updatedTodo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody PublicPost updatedTodo, @PathVariable("id") Long id) {
        publicPostService.delete(updatedTodo);
    }

}
