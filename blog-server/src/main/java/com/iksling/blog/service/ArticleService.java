package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.ArticleBackDTO;
import com.iksling.blog.dto.ArticleOptionDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.vo.ArticleBackVO;

/**
 *
 */
public interface ArticleService extends IService<Article> {

    ArticleBackDTO getBackArticleById(Integer articleId);

    ArticleOptionDTO listArticleOptionDTO();

    Integer saveOrUpdateArticle(ArticleBackVO articleBackVO);
}
