package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.Article;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ArticleVO getBackArticleById(Integer articleId) {
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleContent, Article::getArticleCover, Article::getCategoryId, Article::getIsTop, Article::getIsDraft)
                .eq(Article::getId, articleId)
                .eq(Article::getIsDeleted, 0));
        return null;
    }
}




