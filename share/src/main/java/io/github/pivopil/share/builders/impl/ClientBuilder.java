package io.github.pivopil.share.builders.impl;

/**
 * Created on 14.01.17.
 */


import io.github.pivopil.share.builders.EntityBuilder;
import io.github.pivopil.share.entities.impl.Client;
import io.github.pivopil.share.viewmodels.impl.ClientViewModel;
import net.sf.oval.Validator;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class ClientBuilder implements EntityBuilder<Client, ClientBuilder, ClientViewModel> {
    private Long id;
    private Date created;
    private Date updated;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 100)
    private String clientId;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 100)
    private String clientSecret;

    private String scopes;

    private String roles;

    private Validator ovalValidator;

    public ClientBuilder() {
    }

    @Override
    public ClientViewModel buildViewModel() {
        ClientViewModel clientViewModel = new ClientViewModel();
        clientViewModel.setId(id);
        clientViewModel.setClientId(clientId);
        clientViewModel.setCreated(created);
        clientViewModel.setUpdated(updated);
        clientViewModel.setRoles(roles != null ? Arrays.asList(roles.split(",")) : new ArrayList<>());
        clientViewModel.setScopes(scopes != null ? Arrays.asList(scopes.split(",")) : new ArrayList<>());
        return clientViewModel;
    }

    public ClientBuilder(Client entity) {
        id = entity.getId();
        created = entity.getCreated();
        updated = entity.getUpdated();
        clientId = entity.getClientId();
        clientSecret = entity.getClientSecret();
        scopes = entity.getScopes();
        roles = entity.getRoles();
    }

    @Override
    public ClientBuilder newInstance() {
        return new ClientBuilder();
    }

    @Override
    public ClientBuilder newInstance(Client entity) {
        return new ClientBuilder(entity);
    }

    public ClientBuilder id(Long val) {
        id = val;
        return this;
    }

    public ClientBuilder created(Date val) {
        created = val;
        return this;
    }

    public ClientBuilder updated(Date val) {
        updated = val;
        return this;
    }

    public ClientBuilder clientId(String val) {
        clientId = val;
        return this;
    }

    public ClientBuilder clientSecret(String val) {
        clientSecret = val;
        return this;
    }

    public ClientBuilder scopes(String val) {
        scopes = val;
        return this;
    }

    public ClientBuilder roles(String val) {
        roles = val;
        return this;
    }

    @Override
    public Client build() {

        validate(ovalValidator);

        Client client = new Client();
        client.setId(id);
        client.setCreated(created);
        client.setUpdated(updated);
        client.setClientId(clientId);
        client.setClientSecret(clientSecret);
        client.setScopes(scopes);
        client.setRoles(roles);
        return client;
    }

    @Override
    public ClientBuilder withOvalValidator(Validator ovalValidator) {
        this.ovalValidator = ovalValidator;
        return this;
    }
}
