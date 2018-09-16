package com.jfs.rolemanager.auth.service.impl;

import com.jfs.rolemanager.auth.persistance.model.AppConfig;
import com.jfs.rolemanager.auth.service.eao.TokenService;
import com.jfs.rolemanager.auth.util.JwtTokenUtil;
import com.jfs.rolemanager.common.exception.BaseApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     *
     * @param userName
     * @param roles
     * @param app
     * @return
     * @throws BaseApplicationException
     */
    public String generateJwtToken(String userName, String roles,AppConfig app) throws BaseApplicationException {
        LOGGER.debug("Generating JWT Token for user :{}", userName);
        return jwtTokenUtil.generateToken(userName, roles,app, "web");
    }
}
