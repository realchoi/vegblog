package com.realchoi.vegblogboot.dao;

import com.realchoi.vegblogboot.model.PostTag;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章标签数据操作接口
 */
@Mapper
@Repository
public interface PostTagDao {
    /**
     * 新建文章-标签关系
     *
     * @param postTag 文章标签关系信息
     */
    @Insert("INSERT INTO post_tag VALUES(#{id}, #{postId}, #{postTitle}, #{tagId}, #{tagName}, #{userId}, #{insertTime})")
    void insertPostTag(PostTag postTag);


    /**
     * 根据标签 ID 获取文章-标签关系
     *
     * @param tagId 标签 ID
     * @return
     */
    @Select("SELECT * FROM post_tag WHERE tagId = #{tagId} ORDER BY insertTime")
    List<PostTag> findPostTagsByTagId(@Param("tagId") String tagId);


    /**
     * 根据标签名称获取文章-标签关系
     *
     * @param tagName 标签名称
     * @return
     */
    @Select("SELECT * FROM post_tag WHERE tagName = #{tagName} ORDER BY insertTime")
    List<PostTag> findPostTagsByTagName(@Param("tagName") String tagName);


    /**
     * 根据文章 ID 获取文章-标签关系
     *
     * @param postId 文章 ID
     * @return
     */
    @Select("SELECT * FROM post_tag WHERE postId = #{postId} ORDER BY insertTime")
    List<PostTag> findPostTagsByPostId(@Param("postId") String postId);


    /**
     * 查找一片文章是否已经包含某个标签
     *
     * @param postTag 文章-标签关系
     * @return
     */
    @Select("SELECT COUNT(id) FROM post_tag WHERE postId = #{postId} AND tagId = #{tagId}")
    int findPostTagByPostIdAndTagId(PostTag postTag);

    /**
     * 更新文章时，删除去掉的标签关系
     *
     * @param tagIds
     * @param postId
     */
    @Delete({
            "<script>",
            "DELETE FROM post_tag WHERE postId = #{postId} AND tagId NOT IN",
            "<foreach collection='tagIds' item='tagId' open='(' separator=',' close=')'>",
            "#{tagId}",
            "</foreach>",
            "</script>"
    })
    void deletePostTags(@Param("tagIds") List<String> tagIds, @Param("postId") String postId);


    /**
     * 根据文章 ID 删除该文章的全部文章-标签关系
     *
     * @param postId 文章 ID
     */
    @Delete("DELETE FROM post_tag WHERE postId = #{postId}")
    boolean deletePostTagsByPostId(@Param("postId") String postId);
}
