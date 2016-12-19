package com.pixerf.flickr.model;

/**
 * created by Aamer on 12/17/2016.
 */

public class Error {

    private int code;
    private String message;

    public Error() {

    }

    public Error(String message, int code) {
        this.message = message;
        this.code = code;
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
