package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.iksling.blog.constant.FlagConst.DELETED;
import static com.iksling.blog.constant.FlagConst.HIDDEN;

/**
 *
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public void saveOrUpdateCategoryBackVO(CategoryBackVO categoryBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        Category category = BeanCopyUtil.copyObject(categoryBackVO, Category.class);
        if (category.getId() == null) {
            if (category.getCategoryName() == null)
                throw new OperationStatusException();
            Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                    .eq(Category::getCategoryName, category.getCategoryName())
                    .eq(Category::getUserId, loginUserId)
                    .eq(Category::getDeletedFlag, false));
            if (count > 0)
                throw new OperationStatusException("分类名已存在!");
            category.setUserId(loginUserId);
            category.setCreateUser(loginUserId);
            category.setCreateTime(new Date());
            categoryMapper.insert(category);
        } else {
            Category categoryOrigin = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                    .select(Category::getUserId)
                    .eq(Category::getDeletedFlag, false)
                    .eq(Category::getId, category.getId())
                    .eq(loginUser.getRoleWeight() > 200, Category::getUserId, loginUserId));
            if (categoryOrigin == null)
                throw new OperationStatusException();
            if (category.getCategoryName() != null) {
                Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                        .eq(Category::getCategoryName, category.getCategoryName())
                        .eq(Category::getUserId, categoryOrigin.getUserId())
                        .eq(Category::getDeletedFlag, false));
                if (count > 0)
                    throw new OperationStatusException("分类名已存在!");
            }
            category.setUpdateUser(loginUserId);
            category.setUpdateTime(new Date());
            categoryMapper.updateById(category);
        }
    }

    @Override
    @Transactional
    public void deleteBackCategoriesByIdList(List<Integer> idList) {
        if (idList.isEmpty())
            throw new IllegalRequestException();
        int count = categoryMapper.delete(new LambdaUpdateWrapper<Category>()
                .eq(Category::getDeletedFlag, true)
                .in(Category::getId, idList));
        if (count != idList.size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void updateCategoryStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<Category> lambdaUpdateWrapper = new LambdaUpdateWrapper<Category>()
                .in(Category::getId, statusBackVO.getIdList())
                .eq(Category::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 200, Category::getUserId, loginUser.getUserId());
        if (HIDDEN.equals(statusBackVO.getType()))
            lambdaUpdateWrapper.setSql("hidden_flag = !hidden_flag");
        else
            lambdaUpdateWrapper.setSql("public_flag = !public_flag");
        int count = categoryMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }

    @Override
    @Transactional
    public void updateCategoriesStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        int count = categoryMapper.update(null, new LambdaUpdateWrapper<Category>()
                .set(Category::getDeletedFlag, true)
                .eq(loginUser.getRoleWeight() > 100, Category::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 200, Category::getUserId, loginUser.getUserId())
                .in(Category::getId, statusBackVO.getIdList())
                .notInSql(Category::getId,"select distinct category_id from tb_article where deleted_flag = false")
                .set(Category::getUpdateUser, loginUser.getUserId())
                .set(Category::getUpdateTime, new Date()));
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }

    @Override
    public PagePojo<CategoriesBackDTO> getCategoriesBackDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (DELETED.equals(condition.getType()) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = categoryMapper.selectCategoriesBackDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<CategoriesBackDTO> categoriesBackDTOList = categoryMapper.selectCategoriesBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        return new PagePojo<>(count, categoriesBackDTOList);
    }
}




