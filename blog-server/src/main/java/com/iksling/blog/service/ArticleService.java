package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.ArticleBackDTO;
import com.iksling.blog.dto.ArticleOptionDTO;
import com.iksling.blog.dto.ArticlesBackDTO;
import com.iksling.blog.dto.PageDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.ArticlesGarbageVO;
import com.iksling.blog.vo.ConditionVO;

import java.util.List;

/**
 *
 */
public interface ArticleService extends IService<Article> {

    ArticleBackDTO getArticleBackDTOById(Integer articleId);

    ArticleOptionDTO getArticleOptionDTO();

    Integer saveOrUpdateArticleBackVO(ArticleBackVO articleBackVO);

    PageDTO<ArticlesBackDTO> getPageArticlesBackDTO(ConditionVO condition);

    void updateArticlesDeleteVO(ArticlesGarbageVO articlesGarbageVO);

    void deleteArticleIdList(List<Integer> articleIdList);

    void updateArticleTopById(Integer articleId, Boolean topFlag);

    void updateArticlePublicById(Integer articleId, Boolean publicFlag);

    void updateArticleHiddenById(Integer articleId, Boolean hiddenFlag);

    void updateArticleCommentableById(Integer articleId, Boolean commentableFlag);
}
