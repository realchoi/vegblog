package com.realchoi.vegblogboot.dao;

import com.realchoi.vegblogboot.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LoginTicketDao {
    /**
     * 根据用户 ID 查找用户凭证
     *
     * @param userId 用户 ID
     * @return 用户凭证信息
     */
    @Select("SELECT * FROM loginticket WHERE userId = #{userId}")
    LoginTicket findLoginTicketByUserId(@Param("userId") String userId);

    /**
     * 根据 ID 查找用户凭证
     *
     * @param id ID
     * @return 用户凭证信息
     */
    @Select("SELECT * FROM loginticket WHERE id = #{id}")
    LoginTicket findLoginTicketById(@Param("id") String id);

    /**
     * 根据登录凭证查找用户凭证
     *
     * @param ticket 登录凭证
     * @return 用户凭证信息
     */
    @Select("SELECT * FROM loginticket WHERE ticket = #{ticket}")
    LoginTicket findLoginTicketByTicket(@Param("ticket") String ticket);

    /**
     * 新增用户凭证
     *
     * @param loginTicket 用户凭证信息
     */
    @Insert("INSERT INTO loginticket(id, userId, expired, status, ticket) VALUES(#{id}, #{userId}, #{expired}, #{status}, #{ticket})")
    void insertLoginTicket(LoginTicket loginTicket);

    /**
     * 更新凭证
     *
     * @param loginTicket 凭证信息
     */
    @Update("UPDATE loginTicket SET expired = #{expired}, status = #{status}, ticket = #{ticket} WHERE id = #{id}")
    void updateLoginTicket(LoginTicket loginTicket);

    /**
     * 通过 ID 删除用户凭证
     *
     * @param id ID
     */
    @Delete("DELETE FROM loginticket WHERE id = #{id}")
    void deleteLoginTicketById(@Param("id") String id);
}
