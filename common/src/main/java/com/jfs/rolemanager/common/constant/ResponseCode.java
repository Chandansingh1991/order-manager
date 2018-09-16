package com.jfs.rolemanager.common.constant;

import org.springframework.http.HttpStatus;

public enum ResponseCode {

    INVALID_USER_NAME(4001, "Kindly provide user name", HttpStatus.BAD_REQUEST),

    INVALID_PASSWORD(4002, "Kindly provide password", HttpStatus.BAD_REQUEST),

    EMPTY_TOKEN(4004, "Token cannot be empty", HttpStatus.BAD_REQUEST),

    INVALID_APP_NAME(4005, "Kindly provide valid APP Name", HttpStatus.BAD_REQUEST),


    TOKEN_EXPIRED(4011, "Token in expired", HttpStatus.UNAUTHORIZED),

    TOKEN_INTEGRITY_FAILED(4012, "Token integrity failed", HttpStatus.UNAUTHORIZED),

    TOKEN_INVALID(4013, "Token in not valid anymore", HttpStatus.UNAUTHORIZED),

    INVALID_USER_ROLE(4014, "Selected Role is not assigned to user", HttpStatus.UNAUTHORIZED),

    TOKEN_NOT_FOUND(4015, "Token not found in server", HttpStatus.UNAUTHORIZED),

    USER_NOT_REGISTERED(4016, "User is not registered in System", HttpStatus.UNAUTHORIZED),

    USER_NOT_ACTIVE(4017, "User is not active", HttpStatus.UNAUTHORIZED),

    ROLE_NOT_DEFINED(4018, "No Roles define for user", HttpStatus.UNAUTHORIZED),

    AUTHENTICATION_FAILED(4019, "Username or password is incorrect", HttpStatus.UNAUTHORIZED),

    APP_NAME_NOT_REGISTERED(40110, "App is not registerd in server", HttpStatus.UNAUTHORIZED),


    INTERNAL_SERVER_ERROR(5001, "Internal Server error!!!!", HttpStatus.INTERNAL_SERVER_ERROR),

    SUCCESS(2001, "Authentication successfull", HttpStatus.OK),

    TOKEN_REMOVED_SUCCESSFULLY(2002, "Token removed successfully", HttpStatus.OK);

    private ResponseCode(int code, String description, HttpStatus httpStatus) {

        this.code = code;

        this.description = description;

        this.httpStatus = httpStatus;

    }


    private int code;

    private String description;

    private HttpStatus httpStatus;


    public int getCode() {

        return code;

    }


    public String getDescription() {

        return description;

    }


    public HttpStatus getHttpStatus() {

        return httpStatus;

    }


    @Override

    public String toString() {

        return String.valueOf(this.code);

    }
}
