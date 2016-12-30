package io.github.pivopil.share.entities.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.pivopil.share.entities.BasicEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_table")
public class User extends BasicEntity implements Serializable {

    @NotEmpty
    private String name;

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String login;

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String email;

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String phone;

    @NotEmpty
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    private Boolean enabled;

    public User() {
    }

    public User(User user) {
        super();
        this.setId(user.getId());
        this.name = user.getName();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.enabled = user.getEnabled();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
