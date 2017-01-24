package io.github.pivopil.rest.controllers;

import io.github.pivopil.rest.constants.REST_API;
import io.github.pivopil.rest.services.ContentService;
import io.github.pivopil.share.entities.impl.Content;
import io.github.pivopil.share.viewmodels.impl.ContentViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * Created on 19.10.16.
 */
@RestController
@RequestMapping(REST_API.CONTENT)
public class ContentController {

    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Content>> list(@RequestParam Map<String, String> allRequestParams) {
        String title = allRequestParams.get("title");
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(contentService.getAll(title), headers, HttpStatus.OK);
    }

    @GetMapping(REST_API.ID_PATH_VARIABLE)
    public ContentViewModel getSingle(@PathVariable("id") Long id) {
        return contentService.getContentById(id);
    }

    @PostMapping
    public ContentViewModel create(@RequestBody Content content) {
        return contentService.addContent(content);
    }

    @PutMapping(REST_API.ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Content updatedContent, @PathVariable("id") Long id) {
        updatedContent.setId(id);
        contentService.edit(updatedContent);
    }

    @DeleteMapping(REST_API.ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        contentService.deleteById(id);
    }

}
