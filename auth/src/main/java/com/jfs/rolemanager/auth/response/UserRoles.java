package com.jfs.rolemanager.auth.response;

import com.jfs.rolemanager.common.dto.AbstractBaseDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class UserRoles extends AbstractBaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<RolesPrivileges> rolesPrivileges;
    private String name;

    public List<RolesPrivileges> getRolesPrivileges() {
        return rolesPrivileges;
    }

    public void setRolesPrivileges(List<RolesPrivileges> rolesPrivileges) {
        this.rolesPrivileges = rolesPrivileges;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoles userRoles = (UserRoles) o;
        return Objects.equals(rolesPrivileges, userRoles.rolesPrivileges) &&
                Objects.equals(name, userRoles.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rolesPrivileges, name);
    }
}
