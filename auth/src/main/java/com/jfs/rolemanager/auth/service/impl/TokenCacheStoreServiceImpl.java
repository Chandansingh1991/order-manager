package com.jfs.rolemanager.auth.service.impl;

import com.jfs.rolemanager.auth.service.eao.TokenCacheStoreService;
import com.jfs.rolemanager.common.exception.BaseApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenCacheStoreServiceImpl implements TokenCacheStoreService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private ConcurrentHashMap<String, String> cacheMap;
    private ConcurrentHashMap<String, String> secretMap;

    @Override
    public void saveTokenInCache(String userId, String appName, String token) throws BaseApplicationException {
        LOGGER.debug("Saving Token in Cache for user :{} and app :{}", userId, appName);
        if (cacheMap == null) {
            cacheMap = new ConcurrentHashMap();
        }
        cacheMap.put(getCacheKey(userId, appName), token);
    }

    @Override
    public String fetchTokenFromCache(String userId, String appName) throws BaseApplicationException {
        LOGGER.debug("Fetching Token in Cache for user :{} and app :{}", userId, appName);
        if (!CollectionUtils.isEmpty(cacheMap)) {
            String token = cacheMap.get(getCacheKey(userId, appName));
            if (StringUtils.isEmpty(token)) {
                return "";
            }
            return token;
        }
        return "";
    }

    @Override
    public void removeTokenFromCache(String userId, String appName) throws BaseApplicationException {
        LOGGER.info("Removing Token from Cache for user :{} and app :{}", userId, appName);
        cacheMap.remove(getCacheKey(userId, appName));
    }

    @Override
    public void saveTokenSecretInCache(String appName, String token) throws BaseApplicationException {
        LOGGER.info("Saving Token secret in Cache for app :{}", appName);
        if (secretMap == null) {
            secretMap = new ConcurrentHashMap<>();
        }
        secretMap.put(appName, token);
    }

    @Override
    public String fetchTokenSecretFromCache(String appName) throws BaseApplicationException {
        LOGGER.debug("Fetching Token Secret in Cache for app :{}", appName);
        if (!CollectionUtils.isEmpty(secretMap)) {
            String tokenSecret = secretMap.get(appName);
            if (StringUtils.isEmpty(tokenSecret)) {
                return "";
            }
            return tokenSecret;
        }
        return "";
    }

    @Override
    public void removeTokenSecretFromCache(String appName) throws BaseApplicationException {
        LOGGER.info("Removing Token Secret from Cache for app :{}", appName);
        secretMap.remove(appName);
    }

    /**
     * @param userId
     * @param appName
     * @return
     */
    private String getCacheKey(String userId, String appName) {
        StringBuilder key = new StringBuilder(userId);
        key.append("$$").append(appName);
        return key.toString();
    }
}
