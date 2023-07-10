package com.iksling.blog.service;

import com.iksling.blog.dto.CategoriesBackDTO;
import com.iksling.blog.dto.PageDTO;
import com.iksling.blog.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.vo.ConditionVO;

/**
 *
 */
public interface CategoryService extends IService<Category> {

    PageDTO<CategoriesBackDTO> getPageCategoriesBackDTO(ConditionVO condition);
}
