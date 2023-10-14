package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.ArticlesBackDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Article
 */
public interface ArticleMapper extends BaseMapper<Article> {

    List<ArticlesBackDTO> listArticlesBackDTO(@Param("condition") ConditionVO condition, Integer userId, Integer roleWeight);

    Integer selectArticlesBackDTOCount(@Param("condition") ConditionVO condition, Integer userId, Integer roleWeight);
}




