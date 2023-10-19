package com.iksling.blog.mapper;

import com.iksling.blog.dto.CategoriesBackDTO;
import com.iksling.blog.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.vo.ConditionBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Category
 */
public interface CategoryMapper extends BaseMapper<Category> {
    Integer selectCategoriesBackDTOCount(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);

    List<CategoriesBackDTO> listCategoriesBackDTO(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);
}




