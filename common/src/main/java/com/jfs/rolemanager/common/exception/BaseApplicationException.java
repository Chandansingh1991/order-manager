/*******************************************************************************
 * Copyright (c) 2016, Bharti Airtel Limited. All rights reserved. Bharti Airtel
 * LTD. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *******************************************************************************/

package com.jfs.rolemanager.common.exception;

import com.jfs.rolemanager.common.constant.ResponseCode;
import com.jfs.rolemanager.common.dto.StatusBean;
import org.springframework.http.HttpStatus;

/**
 * @author Chandan Singh Karki
 */
public abstract class BaseApplicationException extends Exception {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The error.
     */
    private StatusBean statusBean;
    private HttpStatus httpStatus;


    public BaseApplicationException(ResponseCode responseCode) {
        super(responseCode.getDescription());
        this.statusBean = new StatusBean(responseCode.getCode(), responseCode.getDescription());
        httpStatus = responseCode.getHttpStatus();
    }

    /**
     * Gets the error.
     *
     * @return the error
     */
    public StatusBean getStatusBean() {
        return statusBean;
    }

    public void setStatusBean(StatusBean statusBean) {
        this.statusBean = statusBean;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
