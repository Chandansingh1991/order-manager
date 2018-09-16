package com.jfs.rolemanager.auth.service.impl;

import org.apache.commons.lang.StringUtils;
import com.jfs.rolemanager.auth.response.RolesPrivileges;
import com.jfs.rolemanager.auth.response.TokenResponse;
import com.jfs.rolemanager.auth.response.UserRoles;
import com.jfs.rolemanager.auth.service.eao.TokenValidatorService;
import com.jfs.rolemanager.auth.util.JwtTokenUtil;
import com.jfs.rolemanager.common.constant.ResponseCode;
import com.jfs.rolemanager.common.exception.AuthException;
import com.jfs.rolemanager.common.exception.BadRequestException;
import com.jfs.rolemanager.common.exception.BaseApplicationException;
import com.jfs.rolemanager.common.services.JsonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class TokenValidatorServiceImpl implements TokenValidatorService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JsonService jsonService;


    @Override
    public boolean validate(String token, String appName) throws BaseApplicationException {
        LOGGER.debug("Validation Token with token id: {}", token);
        if (StringUtils.isEmpty(token)) {
            throw new BadRequestException(ResponseCode.EMPTY_TOKEN);
        } else if (StringUtils.isEmpty(appName)) {
            throw new AuthException(ResponseCode.INVALID_APP_NAME);
        }
        return jwtTokenUtil.validateToken(token, appName);
    }

    @Override
    public boolean validateRolesFromToken(String token, String appName, String action) throws BaseApplicationException {
        LOGGER.info("Fetching roles from Token for app Name :{}", appName);
        if (StringUtils.isEmpty(action)) {
            throw new AuthException(ResponseCode.INVALID_USER_ROLE);
        }
        String roles = jwtTokenUtil.fetchUserRolesFromToken(token, appName);
        TokenResponse tokenResponse = jsonService.getObjectFromJson(roles, TokenResponse.class);
        if (tokenResponse == null || CollectionUtils.isEmpty(tokenResponse.getRoles())) {
            throw new AuthException(ResponseCode.ROLE_NOT_DEFINED);
        }
        LOGGER.debug("Roles fetched successfully from token for app :{}", appName);
        boolean isValidRole = false;
        for (UserRoles userRoles : tokenResponse.getRoles()) {
            for (RolesPrivileges rolesPrivileges : userRoles.getRolesPrivileges()) {
                if (action.equalsIgnoreCase(rolesPrivileges.getName())) {
                    isValidRole = true;
                }
            }
        }
        if (!isValidRole) {
            throw new AuthException(ResponseCode.INVALID_USER_ROLE);
        }
        return true;

    }
}
