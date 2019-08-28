package com.realchoi.vegblogboot.controller;

import com.realchoi.vegblogboot.model.Tag;
import com.realchoi.vegblogboot.model.common.Result;
import com.realchoi.vegblogboot.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * 获取所有的标签
     *
     * @return 标签列表
     */
    @RequestMapping("all")
    public Result findAllTags() {
        Result result = new Result(-1, "查询失败。");
        try {
            List<Tag> allTags = tagService.findAllTags();
            result = new Result(0, "OK", allTags);
        } catch (Exception e) {
            result.setData(e.getMessage());
        }
        return result;
    }
}
