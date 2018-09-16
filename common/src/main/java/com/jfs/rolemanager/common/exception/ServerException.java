package com.jfs.rolemanager.common.exception;


import com.jfs.rolemanager.common.constant.ResponseCode;

public class ServerException extends BaseApplicationException {

    public ServerException(ResponseCode responseCode) {
        super(responseCode);
    }
}
