package io.github.pivopil.rest.controllers;

import io.github.pivopil.rest.constants.REST_API;
import io.github.pivopil.rest.services.CompanyService;
import io.github.pivopil.share.entities.impl.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 08.01.17.
 */
@RestController
@RequestMapping(REST_API.COMPANIES)
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public Iterable<Company> list() {
        return companyService.list();
    }

    @GetMapping(REST_API.ID_PATH_VARIABLE)
    public Company getSingle(@PathVariable("id") Long id) {
        return companyService.getSingle(id);
    }

    @PostMapping
    public Company create(@RequestBody Company company) {
        return companyService.save(company);
    }

    @PutMapping(REST_API.ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Company company, @PathVariable("id") Long id) {
        company.setId(id);
        companyService.update(company);
    }

    @DeleteMapping(REST_API.ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        companyService.delete(id);
    }

}
