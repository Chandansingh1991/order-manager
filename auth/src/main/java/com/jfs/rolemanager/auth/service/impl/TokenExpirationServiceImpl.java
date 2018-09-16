package com.jfs.rolemanager.auth.service.impl;

import org.apache.commons.lang.StringUtils;
import com.jfs.rolemanager.auth.service.eao.TokenExpirationService;
import com.jfs.rolemanager.auth.util.JwtTokenUtil;
import com.jfs.rolemanager.common.constant.ResponseCode;
import com.jfs.rolemanager.common.exception.BadRequestException;
import com.jfs.rolemanager.common.exception.BaseApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenExpirationServiceImpl implements TokenExpirationService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void expireTokenFromCache(String token, String appName) throws BaseApplicationException {
        LOGGER.debug("Expiring Token with token id: {}", token);
        if (StringUtils.isEmpty(token)) {
            throw new BadRequestException(ResponseCode.EMPTY_TOKEN);
        } else if (StringUtils.isEmpty(appName)) {
            throw new BadRequestException(ResponseCode.INVALID_APP_NAME);
        }
        jwtTokenUtil.expireTokenFromCache(token, appName);
    }
}
