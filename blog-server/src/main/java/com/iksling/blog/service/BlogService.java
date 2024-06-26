package com.iksling.blog.service;

import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.TokenVO;

import java.util.HashMap;

/**
 *
 */
public interface BlogService {
    void updateBackAbout(String aboutContent);

    void updateBlogMessageConfig(StatusBackVO statusBackVO);

    HashMap<String, Integer> getBlogMessageConfig();

    Object saveTokenVO(TokenVO tokenVO);

    String getAbout();

    Integer getBlogId(Integer bloggerId);

    HashMap<String, Object> getBlogInfo();
}
