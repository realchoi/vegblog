package com.realchoi.vegblogboot.service;

import com.realchoi.vegblogboot.dao.UserDao;
import com.realchoi.vegblogboot.model.User;
import com.realchoi.vegblogboot.model.LoginTicket;
import com.realchoi.vegblogboot.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final UserDao userDao;
    private final LoginTicketService loginTicketService;

    @Autowired
    public UserService(UserDao userDao, LoginTicketService loginTicketService) {
        this.userDao = userDao;
        this.loginTicketService = loginTicketService;
    }

    /**
     * 查询所有用户信息
     *
     * @return 所有用户信息
     */
    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }

    /**
     * 通过 ID 查询用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    public User findUserById(String id) {
        return userDao.findUserById(id);
    }

    /**
     * 通过用户名查询用户信息
     *
     * @param name 用户名
     * @return 用户信息
     */
    public User findUserByName(String name) {
        return userDao.findUserByName(name);
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     */
    public Map register(User user) {
        Map<String, Object> resultMap;

        // 检查用户名和密码是否为空
        if ((resultMap = checkUserRequired(user)) != null) {
            return resultMap;
        }
        resultMap = new HashMap<>();
        // 检查用户名是否被占用
        if (userDao.findUserByName(user.getName()) != null) {
            resultMap.put("code", -1);
            resultMap.put("message", "用户名已被占用。");
            return resultMap;
        }

        // 随机生成用户 ID
        user.setId(UUID.randomUUID().toString());
        // 随机生成 salt
        user.setSalt(UUID.randomUUID().toString().substring(0, 10));
        // 存储加盐且经过 MD5 转换后的密码
        user.setPassword(Md5Util.md5(user.getPassword() + user.getSalt()));
        userDao.insertUser(user);

        // 生成用户凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        Map ticketMap = loginTicketService.insertLoginTicket(loginTicket);
        if ("-1".equals(ticketMap.get("code"))) {
            resultMap.put("code", -1);
            resultMap.put("message", ticketMap.get("message").toString());
            return resultMap;
        }
        resultMap.put("code", 0);
        resultMap.put("ticket", ticketMap.get("ticket"));
        return resultMap;
    }

    /**
     * 用户登录
     *
     * @param user 用户信息
     */
    public Map login(User user) {
        Map<String, Object> resultMap;
        // 检查用户名和密码是否为空
        if ((resultMap = checkUserRequired(user)) != null) {
            return resultMap;
        }
        resultMap = new HashMap<>();

        User tempUser = userDao.findUserByName(user.getName());
        // 检查用户名是否存在
        if (tempUser == null) {
            resultMap.put("code", -1);
            resultMap.put("message", "用户名不存在。");
            return resultMap;
        }
        // 检查密码是否正确
        if (!tempUser.getPassword().equals(Md5Util.md5(user.getPassword() + tempUser.getSalt()))) {
            resultMap.put("code", -1);
            resultMap.put("message", "密码不正确。");
            return resultMap;
        }
        // 更新用户凭证
        Map ticketMap = loginTicketService.updateLoginTicket(tempUser.getId());
        resultMap.put("code", 0);
        resultMap.put("ticket", ticketMap.get("ticket"));

        return resultMap;
    }


    /**
     * 检测用户名和密码是否为空
     *
     * @param user 输入的用户信息
     * @return 检测结果
     */
    private Map<String, Object> checkUserRequired(User user) {
        Map<String, Object> resultMap = new HashMap<>();
        // 检查用户名和密码是否为空
        if (StringUtils.isEmpty(user.getName())) {
            resultMap.put("code", -1);
            resultMap.put("message", "用户名不能为空。");
            return resultMap;
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            resultMap.put("code", -1);
            resultMap.put("message", "密码不能为空。");
            return resultMap;
        }
        return null;
    }

    /*public void updateUser(String name, String password, String salt, String avatarUrl, String role, String id) {
        authorDao.updateUser(name, password, salt, avatarUrl, role, id);
    }*/

    /**
     * 根据 ID 更新用户信息
     *
     * @param user 用户信息
     */
    @Transactional
    public void updateAuthor(User user) {
        userDao.updateUser(user);
    }

    /**
     * 根据 ID 删除用户信息
     *
     * @param id
     */
    @Transactional
    public void deleteAuthor(String id) {
        userDao.deleteUser(id);
    }
}
