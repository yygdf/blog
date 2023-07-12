package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.PageDTO;
import com.iksling.blog.dto.TagsBackDTO;
import com.iksling.blog.entity.Tag;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ArticleTagMapper;
import com.iksling.blog.mapper.TagMapper;
import com.iksling.blog.service.TagService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.TagBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public PageDTO<TagsBackDTO> getPageTagsBackDTO(ConditionVO condition) {
        Page<Tag> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Tag> tagPage = tagMapper.selectPage(page, new LambdaQueryWrapper<Tag>()
                .select(Tag::getId, Tag::getUserId, Tag::getTagName, Tag::getCreateTime, Tag::getUpdateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()), Tag::getTagName, condition.getKeywords())
                .eq(Tag::getUserId, UserUtil.getLoginUser().getUserId())
                .orderByDesc(Tag::getId));
        return new PageDTO<>((int) tagPage.getTotal(), BeanCopyUtil.copyList(tagPage.getRecords(), TagsBackDTO.class));
    }

    @Override
    @Transactional
    public void deleteTagIdList(List<Integer> tagIdList) {
        articleTagMapper.deleteByTagIdList(tagIdList);
        tagMapper.deleteTagIdList(tagIdList, UserUtil.getLoginUser().getUserId());
    }

    @Override
    public void saveOrUpdateTagBackVO(TagBackVO tagBackVO) {
        Integer userId =  UserUtil.getLoginUser().getUserId();
        Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getTagName, tagBackVO.getTagName())
                .eq(Tag::getUserId, userId));
        if (count > 0)
            throw new OperationStatusException("标签名已存在!");
        Tag tag = new Tag();
        tag.setId(tagBackVO.getId());
        tag.setTagName(tagBackVO.getTagName());
        if (Objects.isNull(tag.getId())) {
            tag.setUserId(userId);
            tag.setCreateUser(userId);
            tag.setCreateTime(new Date());
            tagMapper.insert(tag);
        } else {
            tagMapper.update(null, new LambdaUpdateWrapper<Tag>()
                    .set(Tag::getTagName, tag.getTagName())
                    .set(Tag::getUpdateUser, userId)
                    .set(Tag::getUpdateTime, new Date())
                    .eq(Tag::getId, tag.getId())
                    .eq(Tag::getUserId, userId));
        }
    }
}




