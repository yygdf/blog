package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.CategoriesBackDTO;
import com.iksling.blog.dto.PageDTO;
import com.iksling.blog.entity.Category;
import com.iksling.blog.service.CategoryService;
import com.iksling.blog.mapper.CategoryMapper;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageDTO<CategoriesBackDTO> getPageCategoriesBackDTO(ConditionVO condition) {
        Page<Category> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Category> categoryPage = categoryMapper.selectPage(page, new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getUserId, Category::getCategoryName, Category::getHiddenFlag, Category::getPublicFlag, Category::getCreateTime, Category::getUpdateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()), Category::getCategoryName, condition.getKeywords())
                .eq(Category::getUserId, UserUtil.getLoginUser().getUserId())
                .orderByDesc(Category::getId));
        return new PageDTO<>((int)categoryPage.getTotal(), BeanCopyUtil.copyList(categoryPage.getRecords(), CategoriesBackDTO.class));
    }
}




