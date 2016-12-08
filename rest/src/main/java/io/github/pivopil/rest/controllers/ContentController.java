package io.github.pivopil.rest.controllers;

import io.github.pivopil.rest.constants.REST_API;
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
import java.util.Map;

/**
 * Created on 19.10.16.
 */
@RestController
@RequestMapping(value = REST_API.CONTENT)
public class ContentController {

    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Content>> list(@RequestParam Map<String,String> allRequestParams) {
        String title = allRequestParams.get("title");
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(contentService.getAll(title), headers, HttpStatus.OK);
    }

    @GetMapping(REST_API.ID_PATH_VARIABLE)
    public Content getSingle(@PathVariable("id") Long id) {
        return contentService.getSingle(id);
    }

    @PostMapping
    public Content create(@RequestBody Content todo, @CurrentUser User user) {
        return contentService.add(todo, user);
    }

    @PutMapping(REST_API.ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Content updatedTodo, @PathVariable("id") Long id) throws IOException {
        updatedTodo.setId(id);
        contentService.edit(updatedTodo);
    }

    @DeleteMapping(REST_API.ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        contentService.deleteById(id, user);
    }

}
