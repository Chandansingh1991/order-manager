package com.jfs.rolemanager.auth.request;

import com.jfs.rolemanager.common.dto.AbstractBaseDTO;

public class ValidateTokenRequest extends AbstractBaseDTO {
    private static final long serialVersionUID = 1L;
    private String token;
    private String appName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
