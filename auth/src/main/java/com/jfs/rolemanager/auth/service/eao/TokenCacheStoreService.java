package com.jfs.rolemanager.auth.service.eao;


import com.jfs.rolemanager.common.exception.BaseApplicationException;

public interface TokenCacheStoreService {
    public void saveTokenInCache(String userId, String appName, String token) throws BaseApplicationException;

    public String fetchTokenFromCache(String userId, String appName) throws BaseApplicationException;

    public void removeTokenFromCache(String userId, String appName) throws BaseApplicationException;

    public void saveTokenSecretInCache(String appName, String token) throws BaseApplicationException;

    public String fetchTokenSecretFromCache(String appName) throws BaseApplicationException;

    public void removeTokenSecretFromCache(String appName) throws BaseApplicationException;
}
