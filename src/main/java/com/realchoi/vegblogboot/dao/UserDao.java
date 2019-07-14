package com.realchoi.vegblogboot.dao;

import com.realchoi.vegblogboot.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据操作类
 */
@Mapper
@Repository
public interface UserDao {
    /**
     * 查询所有用户信息
     *
     * @return 所有用户信息
     */
    @Select("SELECT * FROM user")
    List<User> findAllUsers();

    /**
     * 通过 ID 查询用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findUserById(@Param("id") String id);

    /**
     * 通过用户名查询用户信息
     *
     * @param name 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE name = #{name}")
    User findUserByName(@Param("name") String name);


    /**
     * 新增用户信息
     *
     * @param user 用户信息
     */
    @Insert("INSERT INTO user(id, name, password, salt, avatarUrl, role) VALUES (#{id}, #{name}, #{password}, #{salt}, #{avatarUrl}, #{role})")
    void insertUser(User user);


    /**
     * 根据 ID 更新用户信息
     *
     * @param user 用户信息
     */
    @Update("UPDATE user SET name = #{name}, password = #{password}, salt = #{salt}, avatarUrl = #{avatarUrl}, role = #{role} WHERE id = #{id}")
    void updateUser(User user);

    /**
     * 根据 ID 删除用户信息
     *
     * @param id 用户 ID
     */
    @Delete("DELETE from user WHERE id = #{id}")
    void deleteUser(@Param("id") String id);
}
