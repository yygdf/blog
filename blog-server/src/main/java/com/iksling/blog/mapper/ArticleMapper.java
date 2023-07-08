package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.ArticlesBackDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.vo.ConditionVO;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Article
 */
public interface ArticleMapper extends BaseMapper<Article> {

    Integer selectCountByCondition(ConditionVO condition);

    List<ArticlesBackDTO> listArticlesBackDTO(ConditionVO condition);
}




