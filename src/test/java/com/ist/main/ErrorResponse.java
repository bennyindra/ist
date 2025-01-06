package com.ist.main;

import java.util.List;

public class ErrorResponse {
    public ErrorResponse() {}

    public ErrorResponse(String message, int status, List<String> errors) {
        this.setMessage(message);
        this.setStatus(status);
        this.setErrors(errors);
    }

    private String message;
    private int status;
    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
