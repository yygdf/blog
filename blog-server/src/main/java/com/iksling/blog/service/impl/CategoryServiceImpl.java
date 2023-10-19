package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.CategoriesBackDTO;
import com.iksling.blog.entity.Category;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.CategoryMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.CategoryService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.CategoryBackVO;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PagePojo<CategoriesBackDTO> getCategoriesBackDTO(ConditionBackVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (Objects.equals(condition.getType(), 7) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = categoryMapper.selectCategoriesBackDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<CategoriesBackDTO> categoriesBackDTOList = categoryMapper.listCategoriesBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        return new PagePojo<>(count, categoriesBackDTOList);
    }

    @Override
    @Transactional
    public void updateCategoryStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<Category> lambdaUpdateWrapper = new LambdaUpdateWrapper<Category>()
                .in(Category::getId, statusBackVO.getIdList())
                .eq(Category::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 200, Category::getUserId, loginUser.getUserId());
        if (statusBackVO.getType().equals(3))
            lambdaUpdateWrapper.setSql("hidden_flag = !hidden_flag");
        else
            lambdaUpdateWrapper.setSql("public_flag = !public_flag");
        int count = categoryMapper.update(null, lambdaUpdateWrapper);
        if (count != 1)
            throw new OperationStatusException();
    }

    @Override
    @Transactional
    public void deleteBackCategoriesByIdList(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList))
            throw new IllegalRequestException();
        int count = categoryMapper.delete(new LambdaUpdateWrapper<Category>()
                .eq(Category::getDeletedFlag, true)
                .in(Category::getId, idList));
        if (count != idList.size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void saveOrUpdateCategoryBackVO(CategoryBackVO categoryBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Category category = BeanCopyUtil.copyObject(categoryBackVO, Category.class);
        if (Objects.isNull(category.getId())) {
            if (StringUtils.isBlank(category.getCategoryName()))
                throw new OperationStatusException();
            Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                    .eq(Category::getCategoryName, category.getCategoryName())
                    .eq(Category::getUserId, loginUser.getUserId())
                    .eq(Category::getDeletedFlag, false));
            if (count > 0)
                throw new OperationStatusException("分类名已存在!");
            category.setUserId(loginUser.getUserId());
            category.setCreateUser(loginUser.getUserId());
            category.setCreateTime(new Date());
            categoryMapper.insert(category);
        } else {
            if (Objects.nonNull(category.getCategoryName())) {
                if (StringUtils.isBlank(category.getCategoryName()))
                    throw new OperationStatusException();
            }
            Category categoryOrigin = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                    .select(Category::getUserId)
                    .eq(Category::getDeletedFlag, false)
                    .eq(Category::getId, category.getId())
                    .eq(loginUser.getRoleWeight() > 200, Category::getUserId, loginUser.getUserId()));
            if (Objects.isNull(categoryOrigin))
                throw new OperationStatusException();
            if (Objects.nonNull(category.getCategoryName())) {
                Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                        .eq(Category::getCategoryName, category.getCategoryName())
                        .eq(Category::getUserId, categoryOrigin.getUserId())
                        .eq(Category::getDeletedFlag, false));
                if (count > 0)
                    throw new OperationStatusException("分类名已存在!");
            }
            category.setUpdateUser(loginUser.getUserId());
            category.setUpdateTime(new Date());
            categoryMapper.updateById(category);
        }
    }

    @Override
    public void updateCategoriesStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        int count = categoryMapper.update(null, new LambdaUpdateWrapper<Category>()
                .set(Category::getDeletedFlag, true)
                .eq(Category::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 200, Category::getUserId, loginUser.getUserId())
                .in(Category::getId, statusBackVO.getIdList()));
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }
}




