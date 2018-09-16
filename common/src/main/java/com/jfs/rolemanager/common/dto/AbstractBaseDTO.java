package com.jfs.rolemanager.common.dto;


import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * This class defines the common behaviour of all the data transfer object of
 * this application
 *
 * <p>
 * Every data transfer object of this application must extend this class to
 * support the complete ecosystem.
 * @author Chandan Singh Karki
 * </p>
 * <p>
 */
public abstract class AbstractBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        String strObj = new ReflectionToStringBuilder(this).toString();
        return strObj;
    }
}
