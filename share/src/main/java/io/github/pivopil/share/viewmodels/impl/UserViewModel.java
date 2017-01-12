package io.github.pivopil.share.viewmodels.impl;

import io.github.pivopil.share.entities.impl.User;
import io.github.pivopil.share.viewmodels.ViewModel;

import java.util.Date;
import java.util.List;

/**
 * Created on 18.12.16.
 */
public class UserViewModel implements ViewModel<User> {


    private Long id;
    private String user;
    private String login;
    private String name;
    private Boolean enabled;
    private Date created;

    private Date updated;

    private String email;

    private String phone;

    private List<String> roles;

    private String owner;
    private List<String> acls;

    public String getOwner() {
        return owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public List<String> getAcls() {
        return acls;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setAcls(List<String> acls) {
        this.acls = acls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
