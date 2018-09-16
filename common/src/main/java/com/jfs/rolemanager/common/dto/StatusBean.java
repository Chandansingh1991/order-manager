package com.jfs.rolemanager.common.dto;

public class StatusBean extends AbstractBaseDTO {
    private int statusCode;
    private String description;
    private Object result;

    public StatusBean() {
    }

    public StatusBean(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    public StatusBean(int statusCode, String description, Object result) {
        this.statusCode = statusCode;
        this.description = description;
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
