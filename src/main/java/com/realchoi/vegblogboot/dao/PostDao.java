package com.realchoi.vegblogboot.dao;

import com.realchoi.vegblogboot.model.Post;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章数据操作接口
 */
@Mapper
@Repository
public interface PostDao {
    /**
     * 分页查询文章列表
     *
     * @param pageSize 每页数量
     * @param userId   作者 ID
     * @return 文章列表
     */
    @Select("SELECT * FROM post AS t1 " +
            "JOIN (SELECT publishTime FROM post WHERE userId = #{userId} ORDER BY publishTime DESC LIMIT #{total}, 1) AS t2 " +
            "WHERE t1.publishTime <= t2.publishTime AND t1.userId = #{userId} ORDER BY t1.publishTime DESC LIMIT #{pageSize}")
    List<Post> findPostsPaging(@Param("pageSize") int pageSize, @Param("total") int total, @Param("userId") String userId);

    /**
     * 分页查询文章列表，并从一个 postId List 中过滤
     *
     * @param postIds 查找的文章 ID 范围
     * @param pageSize 每页数量
     * @return 文章列表
     */
    @Select({
            "SELECT * FROM post AS t1",
            "JOIN (SELECT publishTime FROM post WHERE id IN (${postIds})",
            "ORDER BY publishTime DESC LIMIT #{total}, 1) AS t2",
            "WHERE t1.publishTime <= t2.publishTime AND t1.id IN (${postIds})",
            "ORDER BY t1.publishTime DESC LIMIT #{pageSize}"
    })
    List<Post> findPostsFromPostIdListPaging(String postIds, @Param("pageSize") int pageSize, @Param("total") int total);


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
    @Insert("INSERT INTO post VALUES(#{id}, #{title}, #{userId}, #{publishTime}, #{updateTime}, #{mdText}, #{content}, #{readTimes}, #{commentCount}, #{likeCount}, #{imageUrl})")
    boolean insertPost(Post post);

    /**
     * 根据 ID 获取文章详细信息
     *
     * @param id 文章 ID
     * @return 文章的详细信息
     */
    @Select("SELECT * FROM post WHERE id = #{id}")
    Post findPostById(@Param("id") String id);

    @Update("UPDATE post SET title = #{title}, content = #{content}, mdText = #{mdText}, updateTime = #{updateTime} WHERE id = #{id}")
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
