package com.iksling.blog.service;

import com.iksling.blog.pojo.Dict;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.TokenVO;

import java.util.Date;
import java.util.HashMap;

/**
 *
 */
public interface BlogService {
    void updateBackAbout(String aboutContent);

    void updateBackBlogMessageConfig(StatusBackVO statusBackVO);

    HashMap<String, Integer> getBackBlogMessageConfig();

    Dict getBackArticleStatistic(Integer userId, Date endDate, Integer days);

    Object saveTokenVO(TokenVO tokenVO);

    String getAbout();

    Integer getBlogId(Integer bloggerId);

    HashMap<String, Object> getBlogInfo();
}
