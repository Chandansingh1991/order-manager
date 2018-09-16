package com.jfs.rolemanager.auth.service.eao;

import com.jfs.rolemanager.common.persistance.model.AppConfig;
import com.jfs.rolemanager.common.exception.BaseApplicationException;

public interface TokenService {

    public String generateJwtToken(String userName, String roles, AppConfig app) throws BaseApplicationException;
}
