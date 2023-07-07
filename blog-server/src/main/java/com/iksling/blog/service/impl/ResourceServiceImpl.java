package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.Resource;
import com.iksling.blog.service.ResourceService;
import com.iksling.blog.mapper.ResourceMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
    implements ResourceService{

}




