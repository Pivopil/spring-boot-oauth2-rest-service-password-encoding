package io.github.pivopil.rest.controllers.domain;

import io.github.pivopil.REST_API;
import io.github.pivopil.rest.services.domain.AdminPostService;
import io.github.pivopil.share.entities.impl.domain.AdminPost;
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
@RequestMapping(value = REST_API.ADMIN_POST)
public class AdminPostController {

    private final AdminPostService adminPostService;

    @Autowired
    public AdminPostController(AdminPostService adminPostService) {
        this.adminPostService = adminPostService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Iterable<AdminPost>> list() {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(adminPostService.getAll(), headers, HttpStatus.OK);
    }

    @PostMapping
    public AdminPost create(@RequestBody AdminPost todo) {
        return adminPostService.add(todo);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody AdminPost updatedTodo, @PathVariable("id") Long id) throws IOException {
        updatedTodo.setId(id);
        adminPostService.edit(updatedTodo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody AdminPost updatedTodo, @PathVariable("id") Long id) {
        adminPostService.delete(updatedTodo);
    }

}
