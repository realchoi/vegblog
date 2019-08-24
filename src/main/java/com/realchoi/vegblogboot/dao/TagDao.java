package com.realchoi.vegblogboot.dao;

import com.realchoi.vegblogboot.model.Tag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 标签数据操作接口
 */
@Mapper
@Repository
public interface TagDao {
    /**
     * 新建标签
     * @param tag 标签信息
     * @return
     */
    @Insert("INSERT INTO tag VALUES(#{id}, #{name})")
    boolean insertTag(Tag tag);


    /**
     * 根据标签名称查找标签信息
     * @param name 标签名称
     * @return
     */
    @Select("SELECT * FROM tag WHERE name = #{name}")
    Tag findTagByName(@Param("name") String name);


    /**
     * 根据标签 id 查找标签信息
     * @param id 标签 ID
     * @return
     */
    @Select("SELECT * FROM tag WHERE id = #{id}")
    Tag findTagById(@Param("id") String id);
}
