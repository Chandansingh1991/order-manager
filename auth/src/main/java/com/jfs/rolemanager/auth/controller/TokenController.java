package com.jfs.rolemanager.auth.controller;

import com.jfs.rolemanager.auth.service.eao.TokenExpirationService;
import com.jfs.rolemanager.auth.service.eao.TokenValidatorService;
import com.jfs.rolemanager.common.constant.ResponseCode;
import com.jfs.rolemanager.common.dto.StatusBean;
import com.jfs.rolemanager.common.exception.AuthException;
import com.jfs.rolemanager.common.exception.BadRequestException;
import com.jfs.rolemanager.common.exception.BaseApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private TokenValidatorService tokenValidatorService;
    @Autowired
    private TokenExpirationService tokenExpirationService;

    /**
     * @param token
     * @param appName
     * @return
     */
    @RequestMapping(value = "/validate/token", method = RequestMethod.GET)
    public ResponseEntity validateToken(@RequestHeader String token, @RequestHeader String appName) {
        LOGGER.info("Request recieved for token validation with token :{}", token);
        StatusBean statusBean = null;
        HttpStatus httpStatus = null;
        try {
            tokenValidatorService.validate(token, appName);
            statusBean = new StatusBean(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDescription());
            statusBean.setResult(true);
            httpStatus = ResponseCode.SUCCESS.getHttpStatus();
        } catch (AuthException e) {
            statusBean = e.getStatusBean();
            httpStatus = e.getHttpStatus();
        } catch (BadRequestException e) {
            statusBean = e.getStatusBean();
            httpStatus = e.getHttpStatus();
        } catch (BaseApplicationException e) {
            LOGGER.error("Exception during token validation :{}", e);
            statusBean = new StatusBean(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), ResponseCode.INTERNAL_SERVER_ERROR.getDescription());
            httpStatus = ResponseCode.INTERNAL_SERVER_ERROR.getHttpStatus();
        }
        return new ResponseEntity(statusBean, httpStatus);
    }

    /**
     * @param token
     * @return
     */
    @RequestMapping(value = "/expire/token", method = RequestMethod.GET)
    public ResponseEntity expireToken(@RequestHeader String token, @RequestHeader String appName) {
        LOGGER.info("Request recieved for token expiration with token :{}", token);
        StatusBean statusBean = null;
        HttpStatus httpStatus = null;
        try {
            tokenExpirationService.expireTokenFromCache(token, appName);
            statusBean = new StatusBean(ResponseCode.TOKEN_REMOVED_SUCCESSFULLY.getCode(), ResponseCode.TOKEN_REMOVED_SUCCESSFULLY.getDescription());
            statusBean.setResult(true);
            httpStatus = ResponseCode.TOKEN_REMOVED_SUCCESSFULLY.getHttpStatus();
        } catch (BadRequestException e) {
            statusBean = e.getStatusBean();
            httpStatus = e.getHttpStatus();
        } catch (AuthException e) {
            statusBean = e.getStatusBean();
            httpStatus = e.getHttpStatus();
        } catch (BaseApplicationException e) {
            LOGGER.error("Exception during token expiration :{}", e);
            statusBean = new StatusBean(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), ResponseCode.INTERNAL_SERVER_ERROR.getDescription());
            httpStatus = ResponseCode.INTERNAL_SERVER_ERROR.getHttpStatus();
        }
        return new ResponseEntity(statusBean, httpStatus);
    }

    /**
     * @param token
     * @return
     */
    @RequestMapping(value = "/validate/role", method = RequestMethod.GET)
    public ResponseEntity validateRolesFromToken(@RequestHeader String token, @RequestHeader String appName, @RequestHeader String action) {
        LOGGER.info("Request recieved for validating role with app :{} and action :{}", appName, action);
        StatusBean statusBean = null;
        HttpStatus httpStatus = null;
        try {
            tokenValidatorService.validate(token, appName);
            boolean isValidRole = tokenValidatorService.validateRolesFromToken(token, appName, action);
            statusBean = new StatusBean(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDescription());
            statusBean.setResult(isValidRole);
            httpStatus = ResponseCode.SUCCESS.getHttpStatus();
        } catch (AuthException e) {
            statusBean = e.getStatusBean();
            httpStatus = e.getHttpStatus();
        } catch (BadRequestException e) {
            statusBean = e.getStatusBean();
            httpStatus = e.getHttpStatus();
        } catch (BaseApplicationException e) {
            LOGGER.error("Exception during token validation :{}", e);
            statusBean = new StatusBean(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), ResponseCode.INTERNAL_SERVER_ERROR.getDescription());
            httpStatus = ResponseCode.INTERNAL_SERVER_ERROR.getHttpStatus();
        }
        return new ResponseEntity(statusBean, httpStatus);
    }

}
