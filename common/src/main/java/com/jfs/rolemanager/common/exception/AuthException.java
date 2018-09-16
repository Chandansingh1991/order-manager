package com.jfs.rolemanager.common.exception;

import com.jfs.rolemanager.common.constant.ResponseCode;

/**
 * @author Chandan Singh Karki
 */
public class AuthException extends BaseApplicationException {
    public AuthException(ResponseCode responseCode) {
        super(responseCode);
    }
}
