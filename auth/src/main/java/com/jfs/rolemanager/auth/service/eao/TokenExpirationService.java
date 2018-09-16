package com.jfs.rolemanager.auth.service.eao;


import com.jfs.rolemanager.common.exception.BaseApplicationException;

public interface TokenExpirationService {
    public void expireTokenFromCache(String token, String appName) throws BaseApplicationException;
}
