package com.realchoi.vegblogboot.service;

import com.realchoi.vegblogboot.dao.PostDao;
import com.realchoi.vegblogboot.dao.PostTagDao;
import com.realchoi.vegblogboot.dao.TagDao;
import com.realchoi.vegblogboot.model.Post;
import com.realchoi.vegblogboot.model.PostTag;
import com.realchoi.vegblogboot.model.Tag;
import com.realchoi.vegblogboot.model.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {
    private final PostDao postDao;
    private final TagDao tagDao;
    private final PostTagDao postTagDao;

    @Autowired
    public PostService(PostDao postDao, TagDao tagDao, PostTagDao postTagDao) {
        this.postDao = postDao;
        this.tagDao = tagDao;
        this.postTagDao = postTagDao;
    }

    /**
     * 分页查询文章列表
     *
     * @param pageIndex 页码
     * @param pageSize  每页数量
     * @param userId    作者 ID
     * @return 文章列表
     */
    public List<Post> findPostsPaging(int pageIndex, int pageSize, String userId) {
        return postDao.findPostsPaging(pageIndex, pageSize, (pageIndex - 1) * pageSize, userId);
    }


    /**
     * 查询文章总数
     *
     * @param userId 作者 ID
     * @return 文章总数
     */
    public int findPoststotalCount(String userId) {
        return postDao.findPoststotalCount(userId);
    }


    /**
     * 发布文章
     *
     * @param post 文章信息
     * @return 发布成功 or 失败
     */
    public Result insertPost(Post post) {
        Result result = checkPostRequired(post);
        if (result.getCode() == -1) {
            return result;
        }
        // 随机生成 ID
        post.setId(UUID.randomUUID().toString());
        // 新文章的评论数量、点赞数量、阅读数量默认为 0
        post.setCommentCount(0);
        post.setLikeCount(0);
        post.setReadTimes(0);
        // 文章的发布时间和更新时间默认为当前时间
        post.setPublishTime(new Date());
        post.setUpdateTime(new Date());

        // 插入数据库
        if (postDao.insertPost(post)) {
            // 插入文章的标签
            // 遍历新发布的文章携带的标签
            DealWithTag(post, 1);
            // 成功则返回文章的 ID
            result.setData(post.getId());
        } else {
            result.setCode(-1);
            result.setMessage("发布失败。");
        }
        return result;
    }


    /**
     * 根据 ID 获取文章详细信息
     *
     * @param id 文章 ID
     * @return
     */
    public Result findPostById(String id) {
        Result result = new Result(0, "OK");
        Post post = postDao.findPostById(id);
        if (post != null) {
            // 查找文章下面的标签
            List<PostTag> postTagList = postTagDao.findPostTagByPostId(post.getId());
            String[] tempTags = new String[postTagList.size()];
            for (int i = 0; i < postTagList.size(); i++) {
                String tagId = postTagList.get(i).getTagId();
                Tag tag = tagDao.findTagById(tagId);
                if (tag != null) {
                    tempTags[i] = tag.getName();
                }
            }
            post.setTag(tempTags);
            result.setData(post);
        } else {
            result.setCode(-1);
            result.setMessage("未找到文章。");
        }
        return result;
    }


    /**
     * 编辑文章
     *
     * @param post 文章信息
     * @return
     */
    public Result updatePost(Post post) {
        Result result = checkPostRequired(post);
        if (result.getCode() == -1) {
            return result;
        }
        // 文章的更新时间默认为当前时间
        post.setUpdateTime(new Date());
        // 修改数据库
        if (postDao.updatePost(post)) {
            // 插入文章的标签
            // 遍历新发布的文章携带的标签
            DealWithTag(post, 2);
            // 成功则返回文章的 ID
            result.setData(post.getId());
        } else {
            result.setCode(-1);
            result.setMessage("编辑失败。");
        }
        return result;
    }

    /**
     * 删除文章
     *
     * @param post 文章信息
     * @return
     */
    public Result deletePostById(Post post) {
        Result result = new Result(0, "OK");
        if (postDao.deletePostById(post.getId())) {
            result.setData(true);
        } else {
            result.setCode(-1);
            result.setMessage("删除失败。");
        }
        return result;
    }


    /**
     * 检查文章的必填项
     *
     * @param post 文章信息
     * @return
     */
    private Result checkPostRequired(Post post) {
        Result result = new Result(0, "OK");
        if (StringUtils.isEmpty(post.getTitle())) {
            result.setCode(-1);
            result.setMessage("文章标题不能为空。");
            return result;
        }
        if (StringUtils.isEmpty(post.getContent())) {
            result.setCode(-1);
            result.setMessage("文章内容不能为空。");
            return result;
        }
        return result;
    }


    /**
     * 新增 or 更新文章时，处理文章的标签
     *
     * @param post   文章的信息
     * @param method 操作方法（值域：1-新增文章；2-更新文章）
     */
    private void DealWithTag(Post post, int method) {
        // 文章携带的所有标签的 ID，供删除标签的 SQL 使用
        List<String> tagIdList = new ArrayList<>();
        // 遍历新发布的文章携带的标签
        for (String tagName : post.getTag()) {
            // 查询数据库中是否已存在当前标签，如果存在则返回标签的 ID
            Tag tempTag = tagDao.findTagByName(tagName);
            String tagId = "";
            if (tempTag != null) {
                tagId = tempTag.getId();
                tagIdList.add(tagId);
            }
            // 如果不存在则新建该标签，并返回新标签的 ID
            else {
                tempTag = new Tag();
                tempTag.setId(UUID.randomUUID().toString());
                tempTag.setName(tagName);
                if (tagDao.insertTag(tempTag)) {
                    tagId = tempTag.getId();
                }
            }

            // 新建文章-标签关系
            PostTag postTag = new PostTag();
            postTag.setId(UUID.randomUUID().toString());
            postTag.setTagId(tagId);
            postTag.setPostId(post.getId());
            postTag.setInsertTime(new Date());

            // 更新文章
            if (method == 2) {
                // 查看当前文章是否已经携带该标签，如果已存在该标签，则不用重复添加
                if (postTagDao.findPostTagByPostIdAndTagId(postTag) <= 0) {
                    postTagDao.insertPostTag(postTag);
                }
            }
            // 新建文章
            else if (method == 1) {
                postTagDao.insertPostTag(postTag);
            }
        }
        // 删除文章的标签
        if (method == 2) {
            if (!tagIdList.isEmpty())
                postTagDao.deletePostTags(tagIdList, post.getId());
            else
                postTagDao.deleteAllPostTags(post.getId());
        }
    }
}
