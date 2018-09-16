package com.jfs.rolemanager.auth.service.impl;

import org.apache.commons.lang.StringUtils;
import com.jfs.rolemanager.auth.persistance.dao.AppConfigRepositry;
import com.jfs.rolemanager.auth.persistance.dao.UserRepository;
import com.jfs.rolemanager.auth.persistance.model.AppConfig;
import com.jfs.rolemanager.auth.persistance.model.Privilege;
import com.jfs.rolemanager.auth.persistance.model.Role;
import com.jfs.rolemanager.auth.persistance.model.User;
import com.jfs.rolemanager.auth.response.RolesPrivileges;
import com.jfs.rolemanager.auth.response.TokenResponse;
import com.jfs.rolemanager.auth.response.UserRoles;
import com.jfs.rolemanager.auth.service.eao.AuthenticateService;
import com.jfs.rolemanager.auth.service.eao.TokenService;
import com.jfs.rolemanager.common.constant.ResponseCode;
import com.jfs.rolemanager.common.exception.AuthException;
import com.jfs.rolemanager.common.exception.BadRequestException;
import com.jfs.rolemanager.common.exception.BaseApplicationException;
import com.jfs.rolemanager.common.services.JsonService;
import com.jfs.rolemanager.ldap.LDAPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class is used to authenticate the user
 *
 * @author Chandan Singh Karki
 */
@Service
public class AuthenticateServiceImpl implements AuthenticateService {
    @Autowired
    private LDAPUtil ldapUtil;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AppConfigRepositry appConfigRepositry;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JsonService jsonService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * @param userName
     * @param password
     * @param appName
     * @return
     * @throws BaseApplicationException
     */
    public TokenResponse authenticateUser(String userName, String password, String appName) throws BaseApplicationException {
        LOGGER.debug("Validating user with username :{} and password :{} for app :{}", userName, password, appName);
        validateInputParmeter(userName, password, appName);
        boolean isValidUser = ldapUtil.authenticate(userName, password, "in\\");
        if (isValidUser) {
            return generateTokenResponse(userName, appName);
        }
        throw new AuthException(ResponseCode.AUTHENTICATION_FAILED);

    }


    /**
     * @param userName
     * @return
     * @throws BaseApplicationException
     */
    private TokenResponse generateTokenResponse(String userName, String appName) throws BaseApplicationException {
        AppConfig appConfig = appConfigRepositry.findByAppName(appName);
        if (appConfig == null) {
            throw new AuthException(ResponseCode.APP_NAME_NOT_REGISTERED);
        }
        User user =null;// userRepository.findByAppNameAndUser(userName, appName);
        if (user == null) {
            throw new AuthException(ResponseCode.USER_NOT_REGISTERED);
        } else if (!user.isEnabled()) {
            throw new AuthException(ResponseCode.USER_NOT_ACTIVE);
        }
        TokenResponse tokenResponse = generateToken(appConfig, user);
        if (tokenResponse != null) {
            userRepository.save(user);
        }
        return tokenResponse;
    }

    /**
     * @param appConfig
     * @param user
     * @return
     * @throws BaseApplicationException
     */
    private TokenResponse generateToken(AppConfig appConfig, User user) throws BaseApplicationException {
        TokenResponse tokenResponse = new TokenResponse();
        List<UserRoles> userRolesList = null;
        LOGGER.debug("Configuring roles for user :{}", user.getUserId());
        if (appConfig.isAuthorizationEnable()) {
            userRolesList = new ArrayList<>();
            UserRoles userRoles = null;
            for (Role role : user.getRoles()) {
                userRoles = new UserRoles();
                userRoles.setName(role.getName());
                RolesPrivileges rolesPrivileges = null;
                List<RolesPrivileges> rolesPrivilegesList = new ArrayList<>();
                for (Privilege privilege : role.getPrivileges()) {
                    rolesPrivileges = new RolesPrivileges();
                    rolesPrivileges.setName(privilege.getName());
                    rolesPrivilegesList.add(rolesPrivileges);
                }
                userRoles.setRolesPrivileges(rolesPrivilegesList);
                userRolesList.add(userRoles);
            }
            LOGGER.debug("Configured roles for user :{} and roles :{}", user.getUserId(), userRoles);
            tokenResponse.setRoles(userRolesList);
        }
        String roles = null;
        if (!CollectionUtils.isEmpty(userRolesList)) {
            roles = jsonService.getJSONString(tokenResponse);
        }
        String token = tokenService.generateJwtToken(user.getUserId(), roles, appConfig);
        tokenResponse.setToken(token);
        return tokenResponse;
    }

    /**
     * @param userName
     * @param password
     * @throws BadRequestException
     */
    private void validateInputParmeter(String userName, String password, String appName) throws BadRequestException {
        if (StringUtils.isEmpty(userName)) {
            throw new BadRequestException(ResponseCode.INVALID_USER_NAME);
        } else if (StringUtils.isEmpty(password)) {
            throw new BadRequestException(ResponseCode.INVALID_PASSWORD);
        } else if (StringUtils.isEmpty(appName)) {
            throw new BadRequestException(ResponseCode.INVALID_APP_NAME);
        }
    }
}
