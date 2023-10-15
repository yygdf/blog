package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.CategoriesBackDTO;
import com.iksling.blog.entity.Category;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.CategoryBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.UpdateBatchVO;

import java.util.List;

/**
 *
 */
public interface CategoryService extends IService<Category> {

    PagePojo<CategoriesBackDTO> getPageCategoriesBackDTO(ConditionBackVO condition);

    void updateCategoryStatusVO(StatusBackVO statusBackVO);

    void deleteCategoryIdList(List<Integer> categoryIdList);

    void saveOrUpdateCategoryBackVO(CategoryBackVO categoryBackVO);

    void updateCategoriesStatus(UpdateBatchVO updateBatchVO);
}
