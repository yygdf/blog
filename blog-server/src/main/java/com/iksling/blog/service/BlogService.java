package com.iksling.blog.service;

import com.iksling.blog.vo.TokenVO;

import java.util.HashMap;

/**
 *
 */
public interface BlogService {
    void updateBackAbout(String aboutContent);

    void saveTokenVO(TokenVO tokenVO);

    String getAbout();

    Integer getBlogId(Integer bloggerId);

    HashMap<String, Object> getBlogInfo();
}
