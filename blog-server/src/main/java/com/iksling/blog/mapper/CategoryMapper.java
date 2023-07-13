package com.iksling.blog.mapper;

import com.iksling.blog.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Category
 */
public interface CategoryMapper extends BaseMapper<Category> {

    void deleteCategoryIdList(List<Integer> categoryIdList, Integer userId, Integer roleWeight);
}




