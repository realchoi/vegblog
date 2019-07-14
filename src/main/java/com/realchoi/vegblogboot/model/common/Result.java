package com.realchoi.vegblogboot.model.common;

/**
 * 结果
 */
public class Result {

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
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


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 结果代码（值域：0-成功；-1-失败；-101-未登录）
     */
    private int code = 0;

    /**
     * 结果说明
     */
    private String message = "OK";

    /**
     * 返回的数据
     */
    private Object data;
}
