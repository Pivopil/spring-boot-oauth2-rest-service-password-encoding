package io.github.pivopil.share.builders.impl;


import io.github.pivopil.share.builders.EntityBuilder;
import io.github.pivopil.share.entities.impl.Role;
import io.github.pivopil.share.entities.impl.User;
import io.github.pivopil.share.viewmodels.UserViewModel;

import java.util.Date;
import java.util.Set;

/**
 * Created on 06.05.16.
 */

public class UserBuilder implements EntityBuilder<User, UserBuilder, UserViewModel> {
    private Long id;
    private Date created;
    private Date updated;
    private String name;
    private String login;
    private String password;
    private Boolean enabled;
    private Set<Role> roles;

    public UserBuilder() {
    }

    public UserBuilder(User entity) {
        id = entity.getId();
        created = entity.getCreated();
        updated = entity.getUpdated();
        name = entity.getName();
        login = entity.getLogin();
        password = entity.getPassword();
        enabled = entity.getEnabled();
        roles = entity.getRoles();
    }

    public UserBuilder(UserViewModel viewModel) {
        id = viewModel.getId();
        created = viewModel.getCreated();
        updated = viewModel.getUpdated();
        name = viewModel.getName();
        login = viewModel.getLogin();
        enabled = viewModel.getEnabled();
//        roles = entity.getRoles();
    }


    @Override
    public UserBuilder newInstance() {
        return new UserBuilder();
    }

    @Override
    public UserBuilder newInstance(User entity) {
        return new UserBuilder(entity);
    }

    @Override
    public UserBuilder newInstance(UserViewModel viewModel) {
        return new UserBuilder(viewModel);
    }

    public UserBuilder id(Long val) {
        id = val;
        return this;
    }

    public UserBuilder created(Date val) {
        created = val;
        return this;
    }

    public UserBuilder updated(Date val) {
        updated = val;
        return this;
    }

    public UserBuilder name(String val) {
        name = val;
        return this;
    }

    public UserBuilder login(String val) {
        login = val;
        return this;
    }

    public UserBuilder password(String val) {
        password = val;
        return this;
    }

    public UserBuilder enabled(Boolean val) {
        enabled = val;
        return this;
    }

    public UserBuilder roles(Set<Role> val) {
        roles = val;
        return this;
    }

    @Override
    public User build() {
        User user = new User();
        user.setId(id);
        user.setCreated(created);
        user.setUpdated(updated);
        user.setName(name);
        user.setLogin(login);
        user.setPassword(password);
        user.setEnabled(enabled);
        user.setRoles(roles);
        return user;
    }
}
