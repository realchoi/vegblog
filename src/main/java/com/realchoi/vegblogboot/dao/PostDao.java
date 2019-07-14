package com.realchoi.vegblogboot.dao;

import com.realchoi.vegblogboot.model.Post;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章数据操作类
 */
@Mapper
@Repository
public interface PostDao {
    /**
     * 分页查询文章列表
     *
     * @param pageIndex 页码
     * @param pageSize  每页数量
     * @param userId    作者 ID
     * @return 文章列表
     */
    @Select("SELECT * FROM post AS t1 " +
            "JOIN (SELECT publishTime FROM post WHERE userId = #{userId} ORDER BY publishTime DESC LIMIT                #{total}, 1) AS t2 " +
            "WHERE t1.publishTime <= t2.publishTime AND t1.userId = #{userId} ORDER BY t1.publishTime " +
            "DESC LIMIT #{pageSize}")
    List<Post> findPostsPaging(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize, @Param("total") int total, @Param("userId") String userId);


    /**
     * 查询文章总数
     *
     * @param userId 作者 ID
     * @return 文章总数
     */
    @Select("SELECT COUNT(id) FROM post WHERE userId = #{userId}")
    int findPoststotalCount(@Param("userId") String userId);


    /**
     * 发布文章
     *
     * @param post 文章信息
     * @return 发布成功 or 失败
     */
    @Insert("INSERT INTO post VALUES(#{id}, #{title}, #{userId}, #{publishTime}, #{updateTime}, #{summary}, #{content}, #{category}, #{tag}, #{readTimes}, #{commentCount}, #{likeCount}, #{imageUrl})")
    boolean insertPost(Post post);

    /**
     * 根据 ID 获取文章详细信息
     *
     * @param id 文章 ID
     * @return 文章的详细信息
     */
    @Select("SELECT * FROM post WHERE id = #{id}")
    Post findPostById(@Param("id") String id);

    @Update("UPDATE post SET title = #{title}, content = #{content}, updateTime = #{updateTime}, category = #{category}, tag = #{tag} WHERE id = #{id}")
    boolean updatePost(Post post);


    /**
     * 查询文章总数
     *
     * @param id 文章 ID
     * @return
     */
    @Delete("DELETE FROM post WHERE id = #{id}")
    boolean deletePostById(@Param("id") String id);
}
