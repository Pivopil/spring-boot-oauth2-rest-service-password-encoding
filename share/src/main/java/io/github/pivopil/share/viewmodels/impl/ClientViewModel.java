package io.github.pivopil.share.viewmodels.impl;

import io.github.pivopil.share.entities.impl.Client;
import io.github.pivopil.share.viewmodels.ViewModel;

import java.util.Date;
import java.util.List;

/**
 * Created on 11.01.17.
 */
public class ClientViewModel implements ViewModel<Client> {

    private Long id;

    private Date created;

    private Date updated;

    private String clientId;

    private List<String> scopes;

    private List<String> roles;

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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
