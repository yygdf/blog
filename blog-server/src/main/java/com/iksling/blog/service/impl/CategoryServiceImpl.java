package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.CategoriesBackDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.Category;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.mapper.CategoryMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.CategoryService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.CategoryBackVO;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
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
    private UserAuthMapper userAuthMapper;

    @Override
    public PagePojo<CategoriesBackDTO> getPageCategoriesBackDTO(ConditionVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (Objects.isNull(condition.getDeletedFlag()))
            condition.setDeletedFlag(false);
        else if (Objects.equals(condition.getDeletedFlag(), true) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        if (loginUser.getRoleWeight() > 200)
            condition.setUserId(loginUser.getUserId());
        Page<Category> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Category> categoryPage = categoryMapper.selectPage(page, new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getUserId, Category::getCategoryName, Category::getHiddenFlag, Category::getPublicFlag, Category::getCreateTime, Category::getUpdateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()), Category::getCategoryName, condition.getKeywords())
                .eq(Category::getDeletedFlag, condition.getDeletedFlag())
                .eq(Objects.nonNull(condition.getUserId()), Category::getUserId, condition.getUserId())
                .orderByDesc(Category::getId));
        if (categoryPage.getTotal() == 0)
            return new PagePojo<>();
        else if (categoryPage.getRecords().size() == 0)
            return new PagePojo<>((int) categoryPage.getTotal(), new ArrayList<>());
        List<CategoriesBackDTO> categoriesBackDTOList = BeanCopyUtil.copyList(categoryPage.getRecords(), CategoriesBackDTO.class);
        List<Integer> userIdList = categoriesBackDTOList.stream().map(CategoriesBackDTO::getUserId).distinct().collect(Collectors.toList());
        List<Map<String, Object>> mapList = userAuthMapper.selectMaps(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUserId, UserAuth::getUsername)
                .in(UserAuth::getUserId, userIdList));
        Map<Integer, String> userIdNameMap = mapList.stream().collect(Collectors.toMap(ml -> (Integer) ml.get("user_id"), ml -> (String) ml.get("username")));
        categoriesBackDTOList.forEach(c -> c.setUsername(userIdNameMap.get(c.getUserId())));
        if (condition.getDeletedFlag())
            categoriesBackDTOList.forEach(c -> c.setArticleCount(0));
        else {
            List<Integer> categoryIdList = categoriesBackDTOList.stream().map(CategoriesBackDTO::getId).collect(Collectors.toList());
            mapList = articleMapper.selectMaps(new QueryWrapper<Article>()
                    .select("category_id, count(1) articleCount")
                    .eq("deleted_flag", false)
                    .in("category_id", categoryIdList)
                    .groupBy("category_id"));
            Map<Integer, Integer> categoryArticleCountMap = mapList.stream().collect(Collectors.toMap(ml -> (Integer) ml.get("category_id"), ml -> (Integer) ml.get("articleCount")));
            categoriesBackDTOList.forEach(c -> c.setArticleCount(castNull204Integer(categoryArticleCountMap.get(c.getId()))));
        }
        return new PagePojo<>((int) categoryPage.getTotal(), categoriesBackDTOList.stream().sorted(Comparator.comparing(CategoriesBackDTO::getArticleCount).reversed()).collect(Collectors.toList()));
    }

    private Integer castNull204Integer(Integer num) {
        return Objects.isNull(num) ? 0 : num;
    }

    @Override
    @Transactional
    public void updateCategoryStatusVO(CommonStatusVO commonStatusVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<Category> lambdaUpdateWrapper = new LambdaUpdateWrapper<Category>()
                .eq(Category::getId, commonStatusVO.getId())
                .eq(Category::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 200, Category::getUserId, loginUser.getUserId());
        if (commonStatusVO.getType().equals(2))
            lambdaUpdateWrapper.setSql("public_flag = !public_flag");
        else if (commonStatusVO.getType().equals(3))
            lambdaUpdateWrapper.setSql("hidden_flag = !hidden_flag");
        else
            throw new OperationStatusException("参数异常!");
        int count = categoryMapper.update(null, lambdaUpdateWrapper);
        if (count != 1)
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void deleteCategoryIdList(List<Integer> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList))
            throw new IllegalRequestException();
        LoginUser loginUser = UserUtil.getLoginUser();
//        count = categoryMapper.deleteCategoryIdList(categoryIdList, loginUser.getUserId(), loginUser.getRoleWeight());
        int count = categoryMapper.delete(new LambdaUpdateWrapper<Category>()
                .eq(Category::getDeletedFlag, true)
                .in(Category::getId, categoryIdList));
        if (count != categoryIdList.size())
            throw new IllegalRequestException();
        articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                .set(Article::getCategoryId, -1)
                .eq(Article::getDeletedFlag, true)
                .in(Article::getCategoryId, categoryIdList));
    }

    @Override
    @Transactional
    public void saveOrUpdateCategoryBackVO(CategoryBackVO categoryBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        categoryBackVO.setCategoryName(categoryBackVO.getCategoryName().trim());
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
            Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                    .eq(Category::getCategoryName, categoryBackVO.getCategoryName())
                    .eq(Category::getUserId, loginUser.getUserId())
                    .ne(Category::getId, categoryBackVO.getId()));
            if (count > 0)
                throw new OperationStatusException("分类名已存在!");
            count = categoryMapper.update(null, new LambdaUpdateWrapper<Category>()
                    .set(Category::getCategoryName, categoryBackVO.getCategoryName())
                    .set(Category::getPublicFlag, categoryBackVO.getPublicFlag())
                    .set(Category::getHiddenFlag, categoryBackVO.getHiddenFlag())
                    .set(Category::getUpdateUser, loginUser.getUserId())
                    .set(Category::getUpdateTime, new Date())
                    .eq(Category::getId, categoryBackVO.getId())
                    .eq(loginUser.getRoleWeight() > 300, Category::getUserId, loginUser.getUserId()));
            if (count != 1)
                throw new IllegalRequestException();
        }
    }

    @Override
    public void updateCategoriesStatus(UpdateBatchVO updateBatchVO) {
        if (CollectionUtils.isEmpty(updateBatchVO.getIdList()))
            throw new IllegalRequestException();
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId())
                .eq(Article::getDeletedFlag, false)
                .in(Article::getCategoryId, updateBatchVO.getIdList()));
        if (count > 0)
            throw new IllegalRequestException();
        count = categoryMapper.deleteCategoryIdList(updateBatchVO.getIdList(), loginUser.getUserId(), loginUser.getRoleWeight());
        if (count != updateBatchVO.getIdList().size())
            throw new IllegalRequestException();
        articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                .set(Article::getCategoryId, -1)
                .eq(Article::getDeletedFlag, true)
                .in(Article::getCategoryId, updateBatchVO.getIdList()));
    }
}




