package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.TagsBackDTO;
import com.iksling.blog.entity.Tag;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ArticleTagMapper;
import com.iksling.blog.mapper.TagMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.TagService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
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
    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    @Transactional
    public void saveOrUpdateTagBackVO(TagBackVO tagBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Tag tag = BeanCopyUtil.copyObject(tagBackVO, Tag.class);
        if (Objects.isNull(tag.getId())) {
            if (StringUtils.isBlank(tag.getTagName()))
                throw new OperationStatusException();
            Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                    .eq(Tag::getTagName, tag.getTagName())
                    .eq(Tag::getUserId, loginUser.getUserId())
                    .eq(Tag::getDeletedFlag, false));
            if (count > 0)
                throw new OperationStatusException("标签名已存在!");
            tag.setUserId(loginUser.getUserId());
            tag.setCreateUser(loginUser.getUserId());
            tag.setCreateTime(new Date());
            tagMapper.insert(tag);
        } else {
            if (Objects.nonNull(tag.getTagName())) {
                if (StringUtils.isBlank(tag.getTagName()))
                    throw new OperationStatusException();
            }
            Tag tagOrigin = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getUserId)
                    .eq(Tag::getDeletedFlag, false)
                    .eq(Tag::getId, tag.getId())
                    .eq(loginUser.getRoleWeight() > 200, Tag::getUserId, loginUser.getUserId()));
            if (Objects.isNull(tagOrigin))
                throw new OperationStatusException();
            if (Objects.nonNull(tag.getTagName())) {
                Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                        .eq(Tag::getTagName, tag.getTagName())
                        .eq(Tag::getUserId, tagOrigin.getUserId())
                        .eq(Tag::getDeletedFlag, false));
                if (count > 0)
                    throw new OperationStatusException("标签名已存在!");
            }
            tag.setUpdateUser(loginUser.getUserId());
            tag.setUpdateTime(new Date());
            tagMapper.updateById(tag);
        }
    }

    @Override
    @Transactional
    public void deleteBackTagsByIdList(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList))
            throw new IllegalRequestException();
        int count = tagMapper.delete(new LambdaUpdateWrapper<Tag>()
                .eq(Tag::getDeletedFlag, true)
                .in(Tag::getId, idList));
        if (count != idList.size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void updateTagsStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        int count = tagMapper.update(null, new LambdaUpdateWrapper<Tag>()
                .set(Tag::getDeletedFlag, true)
                .eq(Tag::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 200, Tag::getUserId, loginUser.getUserId())
                .in(Tag::getId, statusBackVO.getIdList()));
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }

    @Override
    public PagePojo<TagsBackDTO> getTagsBackDTO(ConditionBackVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (Objects.equals(condition.getType(), 7) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = tagMapper.selectTagsBackDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<TagsBackDTO> tagsBackDTOList = tagMapper.selectTagsBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        return new PagePojo<>(count, tagsBackDTOList);
    }
}




