package io.github.pivopil.share.viewmodels.impl;

import io.github.pivopil.share.entities.impl.Company;
import io.github.pivopil.share.viewmodels.ViewModel;

import java.util.Date;

/**
 * Created on 11.01.17.
 */
public class CompanyViewModel implements ViewModel<Company> {

    private Long id;

    private Date created;

    private Date updated;

    private String name;

    private String roleAlias;

    private String description;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public Date getUpdated() {
        return updated;
    }

    @Override
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleAlias() {
        return roleAlias;
    }

    public void setRoleAlias(String roleAlias) {
        this.roleAlias = roleAlias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
