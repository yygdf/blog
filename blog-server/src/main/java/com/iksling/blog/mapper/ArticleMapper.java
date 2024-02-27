package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.ArticleBackDTO;
import com.iksling.blog.dto.ArticlesBackDTO;
import com.iksling.blog.dto.ArticlesDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.vo.ConditionBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Article
 */
public interface ArticleMapper extends BaseMapper<Article> {

    ArticleBackDTO selectArticleBackDTOById(Integer id, Integer userId, Integer roleWeight);

    Integer selectArticlesBackDTOCount(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);

    List<ArticlesBackDTO> selectArticlesBackDTO(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);

    List<ArticlesDTO> selectArticlesDTO(@Param("condition") ConditionBackVO condition);
}




