package com.jfs.rolemanager.auth.service.eao;


import com.jfs.rolemanager.common.exception.BaseApplicationException;

public interface TokenKeyService {
    public String fetchTokenKeyFromCache(String appName) throws BaseApplicationException;

}
