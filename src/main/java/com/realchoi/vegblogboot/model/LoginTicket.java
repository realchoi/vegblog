package com.realchoi.vegblogboot.model;

import java.util.Date;

/**
 * 登录信息表
 */
public class LoginTicket {
    /**
     * ID
     */
    private String id;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 登录过期时间
     */
    private Date expired;

    /**
     * 状态
     */
    private int status;

    /**
     * 凭证
     */
    private String ticket;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
