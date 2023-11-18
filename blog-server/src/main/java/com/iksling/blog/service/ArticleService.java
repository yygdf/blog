package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.ArticleBackDTO;
import com.iksling.blog.dto.ArticleOptionBackDTO;
import com.iksling.blog.dto.ArticlesBackDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.ArticleImageBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.ConditionBackVO;

import java.util.List;

/**
 *
 */
public interface ArticleService extends IService<Article> {

    Integer saveOrUpdateArticleBackVO(ArticleBackVO articleBackVO);

    String saveArticleImageBackVO(ArticleImageBackVO articleImageBackVO);

    void deleteBackArticlesByIdList(List<Integer> idList);

    void updateArticleStatusBackVO(StatusBackVO statusBackVO);

    void updateArticlesStatusBackVO(StatusBackVO statusBackVO);

    void updateBackArticleImagesByFileNameList(List<Long> fileNameList);

    ArticleBackDTO getArticleBackDTOById(Integer id);

    ArticleOptionBackDTO getArticleOptionBackDTO(Integer userId);

    PagePojo<ArticlesBackDTO> getArticlesBackDTO(ConditionBackVO condition);
}
