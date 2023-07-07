package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.Category;
import com.iksling.blog.service.CategoryService;
import com.iksling.blog.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




