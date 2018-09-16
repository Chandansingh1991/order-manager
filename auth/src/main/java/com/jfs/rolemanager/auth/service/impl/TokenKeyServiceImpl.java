package com.jfs.rolemanager.auth.service.impl;

import org.apache.commons.lang.StringUtils;
import com.jfs.rolemanager.auth.persistance.dao.AppConfigRepositry;
import com.jfs.rolemanager.auth.persistance.model.AppConfig;
import com.jfs.rolemanager.auth.service.eao.TokenCacheStoreService;
import com.jfs.rolemanager.auth.service.eao.TokenKeyService;
import com.jfs.rolemanager.common.constant.ResponseCode;
import com.jfs.rolemanager.common.exception.AuthException;
import com.jfs.rolemanager.common.exception.BaseApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenKeyServiceImpl implements TokenKeyService {
    @Autowired
    private TokenCacheStoreService tokenCacheStoreService;
    @Autowired
    private AppConfigRepositry appConfigRepositry;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public String fetchTokenKeyFromCache(String appName) throws BaseApplicationException {
        LOGGER.info("Fetchiing token key from Cache for app :{}", appName);
        String key = tokenCacheStoreService.fetchTokenSecretFromCache(appName);
        if (StringUtils.isEmpty(key)) {
            AppConfig appConfig = appConfigRepositry.findByAppName(appName);
            if (appConfig == null) {
                throw new AuthException(ResponseCode.APP_NAME_NOT_REGISTERED);
            }
            key = appConfig.getTokenKey();
            tokenCacheStoreService.saveTokenSecretInCache(appName, key);
        }
        return key;
    }
}
