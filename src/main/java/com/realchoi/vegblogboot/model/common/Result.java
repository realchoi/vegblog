package com.realchoi.vegblogboot.model.common;

/**
 * 结果
 */
public class Result {

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

    /**
     * 结果代码
     */
    private int code = 0;

    /**
     * 结果说明
     */
    private String message = "OK";
}
