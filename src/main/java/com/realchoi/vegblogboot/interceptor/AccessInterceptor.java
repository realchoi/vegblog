package com.realchoi.vegblogboot.interceptor;

import com.realchoi.vegblogboot.model.LoginTicket;
import com.realchoi.vegblogboot.model.User;
import com.realchoi.vegblogboot.service.LoginTicketService;
import com.realchoi.vegblogboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 访问拦截器
 */
@Component
public class AccessInterceptor implements HandlerInterceptor {
    private final LoginTicketService loginTicketService;
    private final UserService userService;

    /**
     * 当前访问用户的信息
     */
    private User user = null;

    @Autowired
    public AccessInterceptor(LoginTicketService loginTicketService, UserService userService) {
        this.loginTicketService = loginTicketService;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        loadUserInfo(request);
        if (user == null) {
            response.sendRedirect("/user/login");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 获取访问用户的信息
     *
     * @param request 用户请求
     */
    private void loadUserInfo(HttpServletRequest request) {
        // 登录凭证
        String ticket = null;
        // 查看 cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 如果 cookie 有效，则
                if ("ticket".equals(cookie.getName())) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        // 如果获取到登录凭证，根据该登录凭证获取登录人的信息
        if (ticket != null) {
            Map resultMap = this.loginTicketService.findLoginTicketByTicket(ticket);
            if (resultMap.get("ticket") != null) {
                LoginTicket loginTicket = (LoginTicket) resultMap.get("ticket");
                // 登录凭证未失效，切状态正常
                if (loginTicket.getExpired().after(new Date()) && loginTicket.getStatus() == 0) {
                    user = userService.findUserById(loginTicket.getUserId());
                }
            }
        }
    }
}
