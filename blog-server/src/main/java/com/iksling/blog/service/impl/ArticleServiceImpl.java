package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ArticleBackDTO;
import com.iksling.blog.dto.ArticleOptionDTO;
import com.iksling.blog.dto.CategoryDTO;
import com.iksling.blog.dto.TagDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.ArticleTag;
import com.iksling.blog.entity.Category;
import com.iksling.blog.entity.Tag;
import com.iksling.blog.mapper.ArticleTagMapper;
import com.iksling.blog.mapper.CategoryMapper;
import com.iksling.blog.mapper.TagMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.service.ArticleTagService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ArticleBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ArticleBackDTO getBackArticleById(Integer articleId) {
        Integer userId = UserUtil.getLoginUser().getUserId();
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getUserId, Article::getCategoryId, Article::getArticleTitle, Article::getArticleCover, Article::getArticleContent,
                        Article::getIsTop, Article::getIsDraft, Article::getIsPublic, Article::getIsHidden, Article::getIsCommentable)
                .eq(Article::getId, articleId)
                .eq(Article::getUserId, userId)
                .eq(Article::getIsDeleted, 0));
        List<Integer> tagIdList = articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>()
                .select(ArticleTag::getTagId)
                .eq(ArticleTag::getArticleId, articleId))
                .stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        ArticleBackDTO articleBackDTO = BeanCopyUtil.copyObject(article, ArticleBackDTO.class);
        articleBackDTO.setTagIdList(tagIdList);
        return articleBackDTO;
    }

    @Override
    public ArticleOptionDTO listArticleOptionDTO() {
        Integer userId = UserUtil.getLoginUser().getUserId();
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId, Tag::getUserId, Tag::getTagName)
                .eq(Tag::getUserId, userId));
        List<TagDTO> tagDTOList = BeanCopyUtil.copyList(tagList, TagDTO.class);
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getUserId, Category::getCategoryName)
                .eq(Category::getUserId, userId)
                .eq(Category::getIsHidden, 0));
        List<CategoryDTO> categoryDTOList = BeanCopyUtil.copyList(categoryList, CategoryDTO.class);
        return ArticleOptionDTO.builder()
                .userId(userId)
                .tagDTOList(tagDTOList)
                .categoryDTOList(categoryDTOList)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveOrUpdateArticle(ArticleBackVO articleBackVO) {
        LoginUser loginUser =  UserUtil.getLoginUser();
        Article article = BeanCopyUtil.copyObject(articleBackVO, Article.class);
        if (Objects.isNull(article.getId())) {
            article.setUserId(loginUser.getUserId());
            article.setIpSource(loginUser.getIpSource());
            article.setIpAddress(loginUser.getIpAddress());
            article.setCreateUser(loginUser.getUserId());
            article.setCreateTime(new Date());
        } else if (!article.getIsDraft()) {
            article.setUpdateUser(loginUser.getUserId());
            article.setUpdateTime(new Date());
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, article.getId()));
        }
        articleService.saveOrUpdate(article);
        if (!articleBackVO.getTagIdList().isEmpty()) {
            List<ArticleTag> articleTagList = articleBackVO.getTagIdList().stream().map(tagId -> ArticleTag.builder()
                .articleId(article.getId())
                .tagId(tagId)
                .build()).collect(Collectors.toList());
            articleTagService.saveBatch(articleTagList);
        }
        return article.getId();
    }
}




