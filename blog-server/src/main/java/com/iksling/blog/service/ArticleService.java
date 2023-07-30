package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.ArticleBackDTO;
import com.iksling.blog.dto.ArticleOptionDTO;
import com.iksling.blog.dto.ArticlesBackDTO;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.entity.Article;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.ArticleStatusVO;
import com.iksling.blog.vo.UpdateBatchVO;
import com.iksling.blog.vo.ConditionVO;

import java.util.List;

/**
 *
 */
public interface ArticleService extends IService<Article> {

    ArticleBackDTO getArticleBackDTOById(Integer articleId);

    ArticleOptionDTO getArticleOptionDTO(Integer userId);

    Integer saveOrUpdateArticleBackVO(ArticleBackVO articleBackVO);

    PagePojo<ArticlesBackDTO> getPageArticlesBackDTO(ConditionVO condition);

    void updateArticlesStatus(UpdateBatchVO updateBatchVO);

    void deleteArticleIdList(List<Integer> articleIdList);

    void updateArticleStatusVO(ArticleStatusVO articleStatusVO);
}
