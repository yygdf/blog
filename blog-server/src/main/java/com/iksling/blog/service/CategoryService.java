package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.CategoriesBackDTO;
import com.iksling.blog.entity.Category;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.CategoryBackVO;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;

import java.util.List;

/**
 *
 */
public interface CategoryService extends IService<Category> {

    PagePojo<CategoriesBackDTO> getPageCategoriesBackDTO(ConditionVO condition);

    void updateCategoryStatusVO(CommonStatusVO commonStatusVO);

    void deleteCategoryIdList(List<Integer> categoryIdList);

    void saveOrUpdateCategoryBackVO(CategoryBackVO categoryBackVO);

    void updateCategoriesStatus(UpdateBatchVO updateBatchVO);
}
