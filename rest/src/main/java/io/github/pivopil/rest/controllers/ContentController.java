package io.github.pivopil.rest.controllers;

import io.github.pivopil.REST_API;
import io.github.pivopil.rest.handlers.CurrentUser;
import io.github.pivopil.rest.services.ContentService;
import io.github.pivopil.share.entities.impl.Content;
import io.github.pivopil.share.entities.impl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created on 19.10.16.
 */
@RestController
@RequestMapping(value = REST_API.ADMIN_POST)
public class ContentController {

    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Content>> list() {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(contentService.getAll(), headers, HttpStatus.OK);
    }

    @PostMapping
    public Content create(@RequestBody Content todo, @CurrentUser User user) {
        return contentService.add(todo, user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Content updatedTodo, @PathVariable("id") Long id) throws IOException {
        updatedTodo.setId(id);
        contentService.edit(updatedTodo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Content content, @PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        content.setId(id);
        contentService.delete(content, user);
    }

}
