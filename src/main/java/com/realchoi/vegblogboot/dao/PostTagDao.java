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
    @Insert("INSERT INTO post_tag VALUES(#{id}, #{postId}, #{tagId}, #{insertTime})")
    void insertPostTag(PostTag postTag);


    /**
     * 根据文章 ID 获取文章-标签关系
     *
     * @param postId 文章 ID
     * @return
     */
    @Select("SELECT * FROM post_tag WHERE postId = #{postId} ORDER BY insertTime")
    List<PostTag> findPostTagByPostId(@Param("postId") String postId);


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
     * 更新文章时，删除文章的全部标签关系
     *
     * @param postId
     */
    @Delete("DELETE FROM post_tag WHERE postId = #{postId}")
    void deleteAllPostTags(@Param("postId") String postId);
}
