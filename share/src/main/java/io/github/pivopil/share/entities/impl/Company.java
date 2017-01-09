package io.github.pivopil.share.entities.impl;

import io.github.pivopil.share.entities.BasicEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created on 08.01.17.
 */

@Entity
public class Company extends BasicEntity {

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String name;

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String roleAlias;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleAlias() {
        return roleAlias;
    }

    public void setRoleAlias(String roleAlias) {
        this.roleAlias = roleAlias;
    }
}
