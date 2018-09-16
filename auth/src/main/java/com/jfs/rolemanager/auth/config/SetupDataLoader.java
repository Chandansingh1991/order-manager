package com.jfs.rolemanager.auth.config;

import com.jfs.rolemanager.auth.persistance.dao.AppConfigRepositry;
import com.jfs.rolemanager.auth.persistance.dao.PrivilegeRepository;
import com.jfs.rolemanager.auth.persistance.dao.RoleRepository;
import com.jfs.rolemanager.auth.persistance.dao.UserRepository;
import com.jfs.rolemanager.auth.persistance.model.AppConfig;
import com.jfs.rolemanager.auth.persistance.model.Privilege;
import com.jfs.rolemanager.auth.persistance.model.Role;
import com.jfs.rolemanager.auth.persistance.model.User;
import com.jfs.rolemanager.common.constant.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private AppConfigRepositry appConfigRepositry;

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges for lov application
        final Privilege lobUpdate = createPrivilegeIfNotFound("LOV_UPDATE");
        final Privilege lobDelete = createPrivilegeIfNotFound("LOV_DELETE");

        final Privilege docCreate = createPrivilegeIfNotFound("DOC_CREATE");
        final Privilege docUpdate = createPrivilegeIfNotFound("DOC_UPDATE");
        final Privilege docDelete = createPrivilegeIfNotFound("DOC_DELETE");

        final Privilege read = createPrivilegeIfNotFound("READ");
        final Privilege update = createPrivilegeIfNotFound("UPDATE");
        final Privilege delete = createPrivilegeIfNotFound("DELETE");
        final Privilege add = createPrivilegeIfNotFound("ADD");

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<Privilege>(Arrays.asList(lobUpdate, lobDelete, docCreate, docUpdate, docDelete, read));
        final List<Privilege> readOnlyPrivileges = new ArrayList<Privilege>(Arrays.asList(read));
        final List<Privilege> deletePrivileges = new ArrayList<Privilege>(Arrays.asList(lobDelete, docDelete, read));
        final List<Privilege> supeAdminPriveleges = new ArrayList<Privilege>(Arrays.asList(read, update, delete, add));

        AppConfig lovApp = configureAppIfNotFound("LOV", "sharedservices@123");
        AppConfig rolesManagementApp = configureAppIfNotFound("JFS", "jfsrolesmanagement@123");

        final Role cdmAdminRole = createRoleIfNotFound("CDM_ADMIN", adminPrivileges, lovApp);
        final Role cdmReadOnlyRole = createRoleIfNotFound("CDM_READ", readOnlyPrivileges, lovApp);
        final Role cdmDeleteRole = createRoleIfNotFound("CDM_DELETE", deletePrivileges, lovApp);
        final Role superAdmin = createRoleIfNotFound(Roles.SUPER_ADMIN.name(), supeAdminPriveleges, rolesManagementApp);

        lovApp.setRoles(Arrays.asList(cdmAdminRole, cdmReadOnlyRole, cdmDeleteRole));
        rolesManagementApp.setRoles(Arrays.asList(superAdmin));

        // == create initial user
        User himanshu = createUserIfNotFound("himanshu10.gupta", new ArrayList<Role>(Arrays.asList(cdmAdminRole)));
        User garima = createUserIfNotFound("garima.j", new ArrayList<Role>(Arrays.asList(cdmReadOnlyRole)));
        User chandan = createUserIfNotFound("chandan6.singh", new ArrayList<Role>(Arrays.asList(cdmDeleteRole)));

        lovApp.setUsers(Arrays.asList(himanshu, garima, chandan));
        rolesManagementApp.setUsers(Arrays.asList(himanshu));

        appConfigRepositry.saveAndFlush(rolesManagementApp);
        appConfigRepositry.saveAndFlush(lovApp);

        cdmAdminRole.setAppConfig(lovApp);
        cdmDeleteRole.setAppConfig(lovApp);
        cdmReadOnlyRole.setAppConfig(lovApp);
        superAdmin.setAppConfig(rolesManagementApp);

        roleRepository.save(cdmAdminRole);
        roleRepository.save(cdmDeleteRole);
        roleRepository.save(cdmReadOnlyRole);
        roleRepository.save(superAdmin);

        alreadySetup = true;
    }

    @Transactional
    private final Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges, final AppConfig appConfig) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        //role.setAppConfig(appConfig);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    private final User createUserIfNotFound(final String userName, final Collection<Role> roles) {
        User user = userRepository.findByUserId(userName);
        if (user == null) {
            user = new User();
            user.setUserId(userName);
            user.setUsername(userName);
            user.setEmail(userName + "@ril.com");
            user.setCreatedDate(new Date());
            user.setModifiedDate(new Date());
            user.setEnabled(true);
        }
        user.setRoles(roles);
        //user = userRepository.save(user);
        return user;
    }

    @Transactional
    private final AppConfig configureAppIfNotFound(final String appName, final String key) {
        AppConfig appConfig = appConfigRepositry.findByAppName(appName);
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.setAppName(appName);
            appConfig.setTokenKey(key);
            appConfig.setActive(true);
            appConfig.setCreatedDate(new Date());
            appConfig.setModifiedDate(new Date());
            appConfig.setAuthorizationEnable(true);
        }
        return appConfig;
    }

}