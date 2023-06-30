package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.Tag;
import com.iksling.blog.service.TagService;
import com.iksling.blog.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




