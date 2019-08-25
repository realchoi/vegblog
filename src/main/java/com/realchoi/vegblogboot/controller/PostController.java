package com.realchoi.vegblogboot.controller;

import com.realchoi.vegblogboot.model.Post;
import com.realchoi.vegblogboot.model.PostTag;
import com.realchoi.vegblogboot.model.common.Result;
import com.realchoi.vegblogboot.service.PostService;
import com.realchoi.vegblogboot.service.PostTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final PostTagService postTagService;

    @Autowired
    public PostController(PostService postService, PostTagService postTagService) {
        this.postService = postService;
        this.postTagService = postTagService;
    }

    @GetMapping("/list")
    public Result findPostsPaging(int pageIndex, int pageSize, String userId, String tagName) {
        Result result = new Result();
        List<Post> postList = new ArrayList<>();
        // 如果标签 ID 为空，则仅根据用户进行过滤
        if (StringUtils.isEmpty(tagName)) {
            postList = postService.findPostsPaging(pageIndex, pageSize, userId);
        }
        // 否则将同时根据标签进行过滤
        else if (!StringUtils.isEmpty(tagName)) {
            // 先根据当前标签获取文章-标签关联关系
            List<PostTag> postTags = postTagService.findPostTagsByTagName(tagName);
            // 最后根据用户 ID 过滤文章-标签关系，得到当前用户下、当前标签下的文章-标签关系
            // 下面使用了 Java8 的 lambda 表达式
            postTags = postTags.stream()
                    .filter((PostTag postTag) -> postTag.getUserId().equals(userId))
                    .collect(Collectors.toList());
            List<String> postIds;
            if (postTags.size() > 0) {
                postIds = new ArrayList<>();
                for (PostTag postTag : postTags) {
                    postIds.add(postTag.getPostId());
                }
                // 查找这些文章
                String postIdsStr = String.join("','", postIds);
                postList = postService.findPostsFromPostIdListPaging("'" + postIdsStr + "'", pageIndex, pageSize);
            }
        }
        result.setCode(0);
        result.setMessage("OK");
        result.setData(postList);
        return result;
    }

    /**
     * 获取当前用户、当前标签下的所有文章的数量
     *
     * @param userId 当前用户 ID
     * @param tagName  当前标签名称
     * @return 文章的数量
     */
    @GetMapping("/totalCount")
    public Result findPoststotalCount(String userId, String tagName) {
        Result result = new Result();
        int totalCount = 0;
        // 如果标签 ID 为空，则仅根据用户进行过滤
        if (StringUtils.isEmpty(tagName)) {
            totalCount = postService.findPoststotalCount(userId);
        }
        // 否则将同时根据标签进行过滤
        else if (!StringUtils.isEmpty(tagName)) {
            // 先根据当前标签获取文章-标签关联关系
            List<PostTag> postTags = postTagService.findPostTagsByTagName(tagName);
            // 最后根据用户 ID 过滤文章-标签关系，有多少文章-标签关联关系，当前标签下就有多少篇文章
            // 下面使用了 Java8 的 lambda 表达式
            totalCount = postTags.stream()
                    .filter((PostTag postTag) -> postTag.getUserId().equals(userId))
                    .collect(Collectors.toList())
                    .size();
        }
        result.setCode(0);
        result.setMessage("OK");
        result.setData(totalCount);
        return result;
    }


    /**
     * 发布文章
     *
     * @param post 文章信息
     * @return 发布成功 or 失败
     */
    @PostMapping("/create")
    public Result insertPost(@RequestBody Post post) {
        return postService.insertPost(post);
    }

    /**
     * 查询文章详细信息
     *
     * @param id 文章 ID
     * @return 文章详情
     */
    @GetMapping("/detail")
    public Result findPostById(String id) {
        return postService.findPostById(id);
    }

    /**
     * 编辑文章内容
     *
     * @param post 文章信息
     * @return 操作结果
     */
    @PostMapping("/edit")
    public Result updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }


    /**
     * 删除文章
     *
     * @param post 文章信息
     * @return 操作结果
     */
    @PostMapping("/delete")
    public Result deletePostById(@RequestBody Post post) {
        return postService.deletePostById(post);
    }
}
