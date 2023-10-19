package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.CategoriesBackDTO;
import com.iksling.blog.entity.Category;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.CategoryBackVO;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;

import java.util.List;

/**
 *
 */
public interface CategoryService extends IService<Category> {

    void saveOrUpdateCategoryBackVO(CategoryBackVO categoryBackVO);

    void deleteBackCategoriesByIdList(List<Integer> idList);

    void updateCategoryStatusBackVO(StatusBackVO statusBackVO);

    void updateCategoriesStatusBackVO(StatusBackVO statusBackVO);

    PagePojo<CategoriesBackDTO> getCategoriesBackDTO(ConditionBackVO condition);
}
