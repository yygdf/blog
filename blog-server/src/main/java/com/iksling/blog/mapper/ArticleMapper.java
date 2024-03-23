package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.*;
import com.iksling.blog.entity.Article;
import com.iksling.blog.pojo.Condition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Article
 */
public interface ArticleMapper extends BaseMapper<Article> {

    ArticleBackDTO selectArticleBackDTOById(Integer id, Integer userId, Integer roleWeight);

    Integer selectArticlesBackDTOCount(@Param("condition") Condition condition, Integer userId, Integer roleWeight);

    List<ArticlesBackDTO> selectArticlesBackDTO(@Param("condition") Condition condition, Integer userId, Integer roleWeight);

    List<ArticlesDTO> selectArticlesDTO(@Param("condition") Condition condition, boolean loginFlag);

    ArticleDTO selectArticleDTOById(Integer id, Integer bloggerId, boolean flag, boolean loginFlag);

    List<ArticlesPaginationDTO> selectArticlesPaginationDTOById(Integer id, Integer bloggerId, boolean flag);

    List<ArticlesRecommendDTO> selectArticlesRecommendDTOById(Integer id, Integer bloggerId, boolean flag);

    List<ArticlesPreviewDTO> selectArticlesPreviewDTOByCategoryId(@Param("condition") Condition condition);

    List<ArticlesPreviewDTO> selectArticlesPreviewDTOByTagId(@Param("condition") Condition condition);
}




