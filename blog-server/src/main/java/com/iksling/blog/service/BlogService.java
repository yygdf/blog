package com.iksling.blog.service;

import java.util.HashMap;

/**
 *
 */
public interface BlogService {
    void updateBackAbout(String aboutContent);

    String getAbout();

    Integer getBlogId(Integer bloggerId);

    HashMap<String, Object> getBlogInfo();
}
