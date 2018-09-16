package com.jfs.rolemanager.auth.controller;

import com.jfs.rolemanager.auth.request.LoginRequest;
import com.jfs.rolemanager.auth.response.TokenResponse;
import com.jfs.rolemanager.auth.service.eao.AuthenticateService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chandan Singh Karki
 */
@RestController
public class LoginController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    private AuthenticateService authenticateService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        LOGGER.info("Request recieved for login with username :{} and password :{}", new Object[]{loginRequest.getUserId(), loginRequest.getPassword()});
        StatusBean statusBean = null;
        HttpStatus httpStatus = null;
        try {
            TokenResponse response = authenticateService.authenticateUser(loginRequest.getUserId(), loginRequest.getPassword(), loginRequest.getAppName());
            statusBean = new StatusBean(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDescription());
            statusBean.setResult(response);
            httpStatus = ResponseCode.SUCCESS.getHttpStatus();
        } catch (BadRequestException e) {
            statusBean = e.getStatusBean();
            httpStatus = e.getHttpStatus();
        } catch (AuthException e) {
            statusBean = e.getStatusBean();
            httpStatus = e.getHttpStatus();
        } catch (BaseApplicationException e) {
            LOGGER.error("Exception during login for user Id :{} and cause :{}", loginRequest.getUserId(), e);
            statusBean = new StatusBean(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), ResponseCode.INTERNAL_SERVER_ERROR.getDescription());
            httpStatus = ResponseCode.INTERNAL_SERVER_ERROR.getHttpStatus();
        }
        return new ResponseEntity(statusBean, httpStatus);

    }
}
