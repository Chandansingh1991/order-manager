package com.jfs.rolemanager.auth.response;

import com.jfs.rolemanager.common.dto.AbstractBaseDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class TokenResponse extends AbstractBaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
    private List<UserRoles> roles;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<UserRoles> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoles> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenResponse that = (TokenResponse) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {

        return Objects.hash(token, roles);
    }
}
