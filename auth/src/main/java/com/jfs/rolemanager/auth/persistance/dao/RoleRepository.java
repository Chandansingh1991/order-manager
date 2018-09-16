package com.jfs.rolemanager.auth.persistance.dao;

import com.jfs.rolemanager.auth.persistance.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}
