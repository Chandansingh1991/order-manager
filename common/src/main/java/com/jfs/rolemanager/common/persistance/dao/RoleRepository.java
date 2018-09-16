package com.jfs.rolemanager.common.persistance.dao;

import com.jfs.rolemanager.common.persistance.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}
