package io.github.pivopil.share.viewmodels;

import java.util.Date;
import java.util.List;

/**
 * Created on 18.12.16.
 */
public class UserViewModel {


    private Long id;
    private String user;
    private String login;
    private Date created;

    private Date updated;

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
}
