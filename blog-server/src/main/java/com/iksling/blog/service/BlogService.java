package com.iksling.blog.service;

import java.util.HashMap;

/**
 *
 */
public interface BlogService {
    Integer getBlogId(Integer bloggerId);

    HashMap<String, Object> getBlogInfo();
}
