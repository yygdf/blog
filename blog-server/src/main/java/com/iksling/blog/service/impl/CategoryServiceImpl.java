package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.CategoriesBackDTO;
import com.iksling.blog.dto.CategoryArticleDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.Category;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.mapper.CategoryMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.service.CategoryService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.CategoryBackVO;
import com.iksling.blog.vo.CategoryStatusVO;
import com.iksling.blog.vo.ConditionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleService articleService;

    @Override
    public PagePojo<CategoriesBackDTO> getPageCategoriesBackDTO(ConditionVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Page<Category> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Category> categoryPage = categoryMapper.selectPage(page, new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getUserId, Category::getCategoryName, Category::getHiddenFlag, Category::getPublicFlag, Category::getCreateTime, Category::getUpdateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()), Category::getCategoryName, condition.getKeywords())
                .eq(loginUser.getRoleWeight() > 300, Category::getUserId, loginUser.getUserId())
                .orderByDesc(Category::getId));
        List<CategoryArticleDTO> categoryArticleDTOList = articleMapper.selectCategoryArticleCount(loginUser.getUserId(), loginUser.getRoleWeight());
        List<CategoriesBackDTO> categoriesBackDTOList = BeanCopyUtil.copyList(categoryPage.getRecords(), CategoriesBackDTO.class);
        Map<Integer, Integer> categoryArticleMap = categoryArticleDTOList.stream().collect(Collectors.toMap(CategoryArticleDTO::getId, CategoryArticleDTO::getArticleCount, (k1, k2) -> k2));
        categoriesBackDTOList.forEach(c -> c.setArticleCount(categoryArticleMap.get(c.getId())));
        return new PagePojo<>((int)categoryPage.getTotal(), categoriesBackDTOList.stream().sorted(Comparator.comparing(CategoriesBackDTO::getArticleCount).reversed()).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void updateCategoryStatusVO(CategoryStatusVO categoryStatusVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        categoryMapper.update(null, new LambdaUpdateWrapper<Category>()
                .set(Category::getPublicFlag, categoryStatusVO.getPublicFlag())
                .set(Category::getHiddenFlag, categoryStatusVO.getHiddenFlag())
                .eq(Category::getId, categoryStatusVO.getId())
                .eq(loginUser.getRoleWeight() > 300, Category::getUserId, loginUser.getUserId()));
    }

    @Override
    @Transactional
    public void deleteCategoryIdList(List<Integer> categoryIdList) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId())
                .in(Article::getCategoryId, categoryIdList));
        if (count > 0)
            throw new IllegalRequestException("请不要瞎搞, 小心我顺着网线爬过去找你!");
        categoryMapper.deleteCategoryIdList(categoryIdList, loginUser.getUserId(), loginUser.getRoleWeight());
    }

    @Override
    public void saveOrUpdateCategoryBackVO(CategoryBackVO categoryBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (Objects.isNull(categoryBackVO.getId())) {
            Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                    .eq(Category::getCategoryName, categoryBackVO.getCategoryName())
                    .eq(Category::getUserId, loginUser.getUserId()));
            if (count > 0)
                throw new OperationStatusException("分类名已存在!");
            Category category = BeanCopyUtil.copyObject(categoryBackVO, Category.class);
            category.setUserId(loginUser.getUserId());
            category.setCreateUser(loginUser.getUserId());
            category.setCreateTime(new Date());
            categoryMapper.insert(category);
        } else {
            int count = categoryMapper.update(null, new LambdaUpdateWrapper<Category>()
                    .set(Category::getCategoryName, categoryBackVO.getCategoryName())
                    .set(Category::getPublicFlag, categoryBackVO.getPublicFlag())
                    .set(Category::getHiddenFlag, categoryBackVO.getHiddenFlag())
                    .set(Category::getUpdateUser, loginUser.getUserId())
                    .set(Category::getUpdateTime, new Date())
                    .eq(Category::getId, categoryBackVO.getId())
                    .eq(loginUser.getRoleWeight() > 300, Category::getUserId, loginUser.getUserId()));
            if (count != 1)
                throw new IllegalRequestException("请不要瞎搞, 小心我顺着网线爬过去找你!");
        }
    }
}




