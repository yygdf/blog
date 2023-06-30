package com.iksling.blog.service;

import com.iksling.blog.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.vo.ArticleVO;

/**
 *
 */
public interface ArticleService extends IService<Article> {

    ArticleVO getBackArticleById(Integer articleId);
}
