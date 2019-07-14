package com.realchoi.vegblogboot.service;

import com.realchoi.vegblogboot.dao.PostDao;
import com.realchoi.vegblogboot.model.Post;
import com.realchoi.vegblogboot.model.common.Result;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {
    private final PostDao postDao;

    @Autowired
    public PostService(PostDao postDao) {
        this.postDao = postDao;
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
        // 随机生成 ID
        post.setId(UUID.randomUUID().toString());
        // 新文章的评论数量、点赞数量、阅读数量默认为 0
        post.setCommentCount(0);
        post.setLikeCount(0);
        post.setReadTimes(0);
        // 文章的发布时间和更新时间默认为当前时间
        post.setPublishTime(new Date());
        post.setUpdateTime(new Date());
        // 文章的摘要默认从内容中截取 200 字符
        // todo: 后续可以在配置文件中决定文章摘要显示的字数
        post.setSummary(post.getContent().substring(0, 200));

        // 插入数据库
        if (postDao.insertPost(post)) {
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
            result.setData(post);
        } else {
            result.setCode(-1);
            result.setMessage("未找到文章。");
        }
        return result;
    }
}
