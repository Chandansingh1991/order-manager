package com.jfs.rolemanager.auth.service.eao;

import com.jfs.rolemanager.common.exception.BaseApplicationException;

public interface TokenValidatorService {
    public boolean validate(String token, String appName) throws BaseApplicationException;

    public boolean validateRolesFromToken(String token, String appName, String actionName) throws BaseApplicationException;
}
