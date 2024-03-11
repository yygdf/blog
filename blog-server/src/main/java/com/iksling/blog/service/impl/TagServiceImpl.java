package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.TagsBackDTO;
import com.iksling.blog.dto.TagsDTO;
import com.iksling.blog.entity.ArticleTag;
import com.iksling.blog.entity.Tag;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ArticleTagMapper;
import com.iksling.blog.mapper.TagMapper;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.TagService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.TagBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static com.iksling.blog.constant.FlagConst.DELETED;

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

    @Resource
    private HttpServletRequest request;

    @Override
    @Transactional
    public void saveOrUpdateTagBackVO(TagBackVO tagBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        Tag tag = BeanCopyUtil.copyObject(tagBackVO, Tag.class);
        tag.setTagName(tag.getTagName().replace(",", "，"));
        if (tag.getId() == null) {
            Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                    .eq(Tag::getTagName, tag.getTagName())
                    .eq(Tag::getUserId, loginUserId)
                    .eq(Tag::getDeletedFlag, false));
            if (count > 0)
                throw new OperationStatusException("标签名已存在!");
            tag.setUserId(loginUserId);
            tag.setCreateUser(loginUserId);
            tag.setCreateTime(new Date());
            tagMapper.insert(tag);
        } else {
            Tag tagOrigin = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getUserId)
                    .eq(Tag::getDeletedFlag, false)
                    .eq(Tag::getId, tag.getId())
                    .eq(loginUser.getRoleWeight() > 200, Tag::getUserId, loginUserId));
            if (tagOrigin == null)
                throw new OperationStatusException();
            Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                    .eq(Tag::getTagName, tag.getTagName())
                    .eq(Tag::getUserId, tagOrigin.getUserId())
                    .eq(Tag::getDeletedFlag, false));
            if (count > 0)
                throw new OperationStatusException("标签名已存在!");
            tag.setUpdateUser(loginUserId);
            tag.setUpdateTime(new Date());
            tagMapper.updateById(tag);
        }
    }

    @Override
    @Transactional
    public void deleteBackTagsByIdList(List<Integer> idList) {
        if (idList.isEmpty())
            throw new IllegalRequestException();
        int count = tagMapper.delete(new LambdaUpdateWrapper<Tag>()
                .eq(Tag::getDeletedFlag, true)
                .in(Tag::getId, idList));
        if (count != idList.size())
            throw new IllegalRequestException();
        articleTagMapper.delete(new LambdaUpdateWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, idList));
    }

    @Override
    @Transactional
    public void updateTagsStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        int count = tagMapper.update(null, new LambdaUpdateWrapper<Tag>()
                .set(Tag::getDeletedFlag, true)
                .set(loginUser.getRoleWeight() > 100, Tag::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 200, Tag::getUserId, loginUser.getUserId())
                .in(Tag::getId, statusBackVO.getIdList())
                .set(Tag::getUpdateUser, loginUser.getUserId())
                .set(Tag::getUpdateTime, new Date()));
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
        articleTagMapper.update(null, new LambdaUpdateWrapper<ArticleTag>()
                .set(ArticleTag::getDeletedFlag, true)
                .in(ArticleTag::getTagId, statusBackVO.getIdList()));
    }

    @Override
    public PagePojo<TagsBackDTO> getTagsBackDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (DELETED.equals(condition.getType()) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = tagMapper.selectTagsBackDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<TagsBackDTO> tagsBackDTOList = tagMapper.selectTagsBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        return new PagePojo<>(count, tagsBackDTOList);
    }

    @Override
    public PagePojo<TagsDTO> getTagsDTO() {
        Integer bloggerId = Integer.valueOf(request.getHeader("Blogger-Id"));
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId, Tag::getUserId, Tag::getTagName)
                .eq(Tag::getDeletedFlag, false)
                .eq(Tag::getUserId, bloggerId));
        if (tagList.isEmpty())
            return new PagePojo<>();
        return new PagePojo<>(tagList.size(), BeanCopyUtil.copyList(tagList, TagsDTO.class));
    }
}




