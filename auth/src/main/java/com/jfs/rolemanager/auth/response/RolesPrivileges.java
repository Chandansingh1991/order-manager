package com.jfs.rolemanager.auth.response;

import com.jfs.rolemanager.common.dto.AbstractBaseDTO;

import java.io.Serializable;
import java.util.Objects;

public class RolesPrivileges extends AbstractBaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

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
        RolesPrivileges that = (RolesPrivileges) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
