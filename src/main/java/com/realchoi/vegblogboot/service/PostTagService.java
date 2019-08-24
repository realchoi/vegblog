package com.realchoi.vegblogboot.service;

import com.realchoi.vegblogboot.dao.PostTagDao;
import com.realchoi.vegblogboot.model.PostTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostTagService {
    private final PostTagDao postTagDao;

    @Autowired
    public PostTagService(PostTagDao postTagDao) {
        this.postTagDao = postTagDao;
    }


    /**
     * 根据标签 ID 查找文章-标签关联关系
     *
     * @param tagId 标签 ID
     * @return
     */
    public List<PostTag> findPostTagsByTagId(String tagId) {
        return this.postTagDao.findPostTagsByTagId(tagId);
    }
}
