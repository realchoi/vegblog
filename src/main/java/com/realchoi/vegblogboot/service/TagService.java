package com.realchoi.vegblogboot.service;

import com.realchoi.vegblogboot.dao.TagDao;
import com.realchoi.vegblogboot.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    /**
     * 查找所有的标签
     *
     * @return
     */
    public List<Tag> findAllTags() {
        return tagDao.findAllTags();
    }
}
