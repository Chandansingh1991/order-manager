package com.jfs.rolemanager.auth.persistance.dao;

import com.jfs.rolemanager.auth.persistance.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);

    @Override
    void delete(Privilege privilege);

}
