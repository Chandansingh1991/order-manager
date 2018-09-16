package com.jfs.rolemanager.auth.service.eao;


import com.jfs.rolemanager.auth.response.TokenResponse;
import com.jfs.rolemanager.common.exception.BaseApplicationException;

/**
 * @author Chandan Singh Karki
 */
public interface AuthenticateService {

    public TokenResponse authenticateUser(String userName, String password, String appName) throws BaseApplicationException;
}
