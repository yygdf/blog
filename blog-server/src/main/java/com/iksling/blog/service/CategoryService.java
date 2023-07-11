package com.iksling.blog.service;

import com.iksling.blog.dto.CategoriesBackDTO;
import com.iksling.blog.dto.PageDTO;
import com.iksling.blog.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.vo.CategoryBackVO;
import com.iksling.blog.vo.CategoryStatusVO;
import com.iksling.blog.vo.ConditionVO;

import java.util.List;

/**
 *
 */
public interface CategoryService extends IService<Category> {

    PageDTO<CategoriesBackDTO> getPageCategoriesBackDTO(ConditionVO condition);

    void updateCategoryStatusVO(CategoryStatusVO categoryStatusVO);

    void deleteCategoryIdList(List<Integer> categoryIdList);

    void saveOrUpdateCategoryBackVO(CategoryBackVO categoryBackVO);
}
