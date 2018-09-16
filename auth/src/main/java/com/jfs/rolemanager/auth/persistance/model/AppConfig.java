package com.jfs.rolemanager.auth.persistance.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "app")
public class AppConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "appName")
    private String appName;
    @Column(name = "token_key")
    private String tokenKey;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "modified_date")
    private Date modifiedDate;
    @Column(name = "is_active")
    private boolean active;
    @Column(name = "authorization_enable")
    private boolean authorizationEnable;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "app_users", joinColumns = @JoinColumn(name = "appName", referencedColumnName = "appName"), inverseJoinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId"))
    private Collection<User> users;

    @OneToMany(mappedBy = "appConfig")
    private Collection<Role> roles;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public boolean isAuthorizationEnable() {
        return authorizationEnable;
    }

    public void setAuthorizationEnable(boolean authorizationEnable) {
        this.authorizationEnable = authorizationEnable;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
