package io.chelizi.amokhttp.entity;

/**
 * Created by Eddie on 2017/5/20.
 */

public class HttpError {

    private int error_code;
    private String error_message;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
