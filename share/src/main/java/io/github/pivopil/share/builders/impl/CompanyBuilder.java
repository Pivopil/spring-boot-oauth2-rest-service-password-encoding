package io.github.pivopil.share.builders.impl;

/**
 * Created on 14.01.17.
 */


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.pivopil.share.builders.EntityBuilder;
import io.github.pivopil.share.entities.impl.Company;
import io.github.pivopil.share.viewmodels.impl.CompanyViewModel;
import net.sf.oval.Validator;

import java.util.Date;

public class CompanyBuilder implements EntityBuilder<Company, CompanyBuilder, CompanyViewModel> {
    private Long id;
    private Date created;
    private Date updated;
    private String name;
    private String roleAlias;
    private String description;

    @JsonIgnore
    private Validator ovalValidator;

    public CompanyBuilder() {
    }

    @Override
    public CompanyViewModel buildViewModel() {
        CompanyViewModel companyViewModel = new CompanyViewModel();
        companyViewModel.setId(id);
        companyViewModel.setCreated(created);
        companyViewModel.setUpdated(updated);
        companyViewModel.setName(name);
        companyViewModel.setRoleAlias(roleAlias);
        companyViewModel.setDescription(description);
        return companyViewModel;
    }

    public CompanyBuilder(Company entity) {
        id = entity.getId();
        created = entity.getCreated();
        updated = entity.getUpdated();
        name = entity.getName();
        roleAlias = entity.getRoleAlias();
        description = entity.getDescription();
    }

    @Override
    public CompanyBuilder newInstance() {
        return new CompanyBuilder();
    }

    @Override
    public CompanyBuilder newInstance(Company entity) {
        return new CompanyBuilder(entity);
    }

    public CompanyBuilder id(Long val) {
        id = val;
        return this;
    }

    public CompanyBuilder created(Date val) {
        created = val;
        return this;
    }

    public CompanyBuilder updated(Date val) {
        updated = val;
        return this;
    }

    public CompanyBuilder name(String val) {
        name = val;
        return this;
    }

    public CompanyBuilder roleAlias(String val) {
        roleAlias = val;
        return this;
    }

    public CompanyBuilder description(String val) {
        description = val;
        return this;
    }

    @Override
    public Company build() {
        Company company = new Company();
        company.setId(id);
        company.setCreated(created);
        company.setUpdated(updated);
        company.setName(name);
        company.setRoleAlias(roleAlias);
        company.setDescription(description);
        return company;
    }

    @Override
    public CompanyBuilder withOvalValidator(Validator ovalValidator) {
        this.ovalValidator = ovalValidator;
        return this;
    }
}
