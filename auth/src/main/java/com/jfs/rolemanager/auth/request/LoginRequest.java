package com.jfs.rolemanager.auth.request;

import com.jfs.rolemanager.common.dto.AbstractBaseDTO;

/**
 * @author Chandan Singh Karki
 */
public class LoginRequest extends AbstractBaseDTO {
    private static final long serialVersionUID = 1L;
    private String appName;
    private String userId;
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
