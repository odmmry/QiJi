package com.example.myapplication.model.response;

public class BaseResponse {

    private int code;

    private String message;

    public boolean isSucceeded() {
        return code == 200;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}