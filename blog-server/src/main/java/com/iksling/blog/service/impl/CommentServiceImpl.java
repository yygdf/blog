package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.Comment;
import com.iksling.blog.service.CommentService;
import com.iksling.blog.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




