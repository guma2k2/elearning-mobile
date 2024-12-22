package com.example.elearningmobile.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ErrorVm implements Serializable {
    private String statusCode,  title, details;
    private List<String> fieldErrors = new ArrayList<>();

    public ErrorVm() {
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
