package io.github.pivopil.share.builders.impl;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.pivopil.share.builders.EntityBuilder;
import io.github.pivopil.share.entities.impl.Role;
import io.github.pivopil.share.entities.impl.User;
import io.github.pivopil.share.viewmodels.impl.UserViewModel;
import net.sf.oval.Validator;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on 06.05.16.
 */

public class UserBuilder implements EntityBuilder<User, UserBuilder, UserViewModel> {
    public static final String REGEX = "[a-fA-F0-9]{40}";
    private Long id;
    private Date created;
    private Date updated;
    private String name;
    private String login;
    private String password;
    private Boolean enabled;
    private String phone;
    private String email;
    private Set<Role> roles;

    @JsonIgnore
    private Validator ovalValidator;

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
        phone = entity.getPhone();
        email = entity.getEmail();
    }


    @Override
    public UserBuilder newInstance() {
        return new UserBuilder();
    }

    @Override
    public UserBuilder newInstance(User entity) {
        return new UserBuilder(entity);
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

    public UserBuilder email(String val) {
        email = val;
        return this;
    }

    public UserBuilder phone(String val) {
        phone = val;
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

        if (!isValidSHA1(password)) {
            // todo: validate row password
        }

        user.setEmail(email);
        user.setPhone(phone);

        user.setPassword(password);
        user.setEnabled(enabled);
        user.setRoles(roles);
        return user;
    }

    @Override
    public UserBuilder withOvalValidator(Validator ovalValidator) {
        this.ovalValidator = ovalValidator;
        return this;
    }

    private boolean isValidSHA1(String s) {
        return s.matches(REGEX);
    }

    @Override
    public UserViewModel buildViewModel() {
        UserViewModel userViewModel = new UserViewModel();
        userViewModel.setId(id);
        userViewModel.setCreated(created);
        userViewModel.setUpdated(updated);
        userViewModel.setName(name);
        userViewModel.setLogin(login);
        userViewModel.setEnabled(enabled);
        userViewModel.setEmail(email);
        userViewModel.setPhone(phone);
        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
        userViewModel.setRoles(roleNames);
        return userViewModel;
    }
}
