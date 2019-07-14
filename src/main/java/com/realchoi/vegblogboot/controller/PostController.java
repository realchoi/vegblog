package com.realchoi.vegblogboot.controller;

import com.realchoi.vegblogboot.model.Post;
import com.realchoi.vegblogboot.model.common.Result;
import com.realchoi.vegblogboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/list")
    public Result findPostsPaging(int pageIndex, int pageSize, String userId) {
        Result result = new Result();
        List<Post> postList = postService.findPostsPaging(pageIndex, pageSize, userId);
        result.setCode(0);
        result.setMessage("OK");
        result.setData(postList);
        return result;
    }

    @GetMapping("/totalCount")
    public Result findPoststotalCount(String userId) {
        Result result = new Result();
        int totalCount = postService.findPoststotalCount(userId);
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
     * @return
     */
    @GetMapping("/detail")
    public Result findPostById(String id) {
        return postService.findPostById(id);
    }

    /**
     * 编辑文章内容
     *
     * @param post 文章信息
     * @return
     */
    @PostMapping("/edit")
    public Result updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }


    /**
     * 删除文章
     *
     * @param post 文章信息
     * @return
     */
    @PostMapping("/delete")
    public Result deletePostById(@RequestBody Post post) {
        return postService.deletePostById(post);
    }
}
