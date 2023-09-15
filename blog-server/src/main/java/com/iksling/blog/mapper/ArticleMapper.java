package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.ArticlesBackDTO;
import com.iksling.blog.dto.CategoryArticleDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.vo.UpdateBatchVO;
import com.iksling.blog.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Article
 */
public interface ArticleMapper extends BaseMapper<Article> {

    List<ArticlesBackDTO> listArticlesBackDTO(@Param("condition") ConditionVO condition, Integer userId, Integer roleWeight);

    Integer updateArticlesStatus(@Param("updateBatch") UpdateBatchVO updateBatch, Integer userId, Integer roleWeight);

    List<CategoryArticleDTO> selectCategoryArticleCount(@Param("categoryIdList") List<Integer> categoryIdList);

    Integer selectArticlesBackDTOCount(@Param("condition") ConditionVO condition, Integer userId, Integer roleWeight);
}




