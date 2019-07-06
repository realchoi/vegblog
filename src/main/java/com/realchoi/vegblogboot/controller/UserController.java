package com.realchoi.vegblogboot.controller;

import com.realchoi.vegblogboot.model.LoginTicket;
import com.realchoi.vegblogboot.model.User;
import com.realchoi.vegblogboot.model.common.Result;
import com.realchoi.vegblogboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static java.lang.Math.toIntExact;

/**
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     *
     * @param response 响应结果
     * @param user     用户信息
     * @return 注册结果
     */
    @PostMapping("register")
    public Result register(HttpServletResponse response, @RequestBody User user) {
        // 注册结果
        Result result = new Result();
        Map resultMap = userService.register(user);
        if ("-1".equals(resultMap.get("code").toString())) {
            result.setCode(-1);
            result.setMessage(resultMap.get("message").toString());
        } else {
            // 注册成功后，设置 cookie

            result.setCode(0);
            result.setMessage("注册成功。");
        }
        return result;
    }


    /**
     * 用户登录
     *
     * @param response 响应结果
     * @param user     用户信息
     * @return 登录结果
     */
    @PostMapping("login")
    public Result login(HttpServletResponse response, @RequestBody User user) {
        // 登录结果
        Result result = new Result();
        Map resultMap = userService.login(user);
        if ("-1".equals(resultMap.get("code").toString())) {
            result.setCode(-1);
            result.setMessage(resultMap.get("message").toString());
        } else {
            // 登录成功后，设置 cookie
            Cookie cookie = new Cookie("ticket", ((LoginTicket) resultMap.get("ticket")).getTicket());
            cookie.setPath("/");
            cookie.setMaxAge(toIntExact((((LoginTicket) resultMap.get("ticket")).getExpired().getTime() - System.currentTimeMillis()) / 1000));
            response.addCookie(cookie);

            result.setCode(0);
            result.setMessage("登录成功。");
        }
        return result;
    }

    @GetMapping("all")
    public List<User> findAllAuthors() {
        return userService.findAllAuthors();
    }

    @GetMapping("find")
    public User findAuthorById(String id) {
        return userService.findUserById(id);
    }


}