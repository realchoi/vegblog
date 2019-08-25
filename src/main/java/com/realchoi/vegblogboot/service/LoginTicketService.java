package com.realchoi.vegblogboot.service;

import com.realchoi.vegblogboot.dao.LoginTicketDao;
import com.realchoi.vegblogboot.model.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginTicketService {
    private final LoginTicketDao loginTicketDao;

    @Autowired
    public LoginTicketService(LoginTicketDao loginTicketDao) {
        this.loginTicketDao = loginTicketDao;
    }

    /**
     * 新增凭证
     *
     * @param loginTicket 凭证信息
     * @return 包含用户凭证信息的 Map
     */
    Map insertLoginTicket(LoginTicket loginTicket) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isEmpty(loginTicket.getUserId())) {
            resultMap.put("code", -1);
            resultMap.put("message", "用户 ID 不能为空。");
            return resultMap;
        }

        // 随机生成 ID
        loginTicket.setId(UUID.randomUUID().toString());
        // 过期时间设为一小时后
        //loginTicket.setExpired(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
        loginTicket.setExpired(new Date(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() + 60 * 60 * 1000));
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString());
        this.loginTicketDao.insertLoginTicket(loginTicket);

        resultMap.put("code", 0);
        resultMap.put("ticket", loginTicket);
        return resultMap;
    }

    /**
     * 通过用户 ID 更新凭证
     *
     * @param userId 用户 ID
     * @return 包含用户凭证信息的 Map
     */
    Map updateLoginTicket(String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        LoginTicket loginTicket = this.loginTicketDao.findLoginTicketByUserId(userId);
        loginTicket.setTicket(UUID.randomUUID().toString());
        loginTicket.setStatus(0);
        //loginTicket.setExpired(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
        loginTicket.setExpired(new Date(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() + 60 * 60 * 1000));
        this.loginTicketDao.updateLoginTicket(loginTicket);
        resultMap.put("code", 0);
        resultMap.put("ticket", loginTicket);
        return resultMap;
    }

    /**
     * 根据登录凭证查找用户凭证
     *
     * @param ticket 登录凭证
     * @return 包含用户凭证信息的 Map
     */
    public Map findLoginTicketByTicket(String ticket) {
        Map<String, Object> resultMap = new HashMap<>();
        LoginTicket loginTicket = this.loginTicketDao.findLoginTicketByTicket(ticket);
        if (loginTicket != null) {
            resultMap.put("code", 0);
            resultMap.put("ticket", loginTicket);
        } else {
            resultMap.put("code", -1);
            resultMap.put("msg", "未找到用户登录凭证。");
        }
        return resultMap;
    }
}
