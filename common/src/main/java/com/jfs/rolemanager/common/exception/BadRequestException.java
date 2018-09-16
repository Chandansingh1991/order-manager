package com.jfs.rolemanager.common.exception;


import com.jfs.rolemanager.common.constant.ResponseCode;

public class BadRequestException extends BaseApplicationException {


    public BadRequestException(ResponseCode responseCode) {
        super(responseCode);
    }
}
