package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.TagsBackDTO;
import com.iksling.blog.entity.Tag;
import com.iksling.blog.entity.UserAuth;
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
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.TagBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public PagePojo<TagsBackDTO> getPageTagsBackDTO(ConditionVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Page<Tag> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Tag> tagPage = tagMapper.selectPage(page, new LambdaQueryWrapper<Tag>()
                .select(Tag::getId, Tag::getUserId, Tag::getTagName, Tag::getCreateTime, Tag::getUpdateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()), Tag::getTagName, condition.getKeywords())
                .eq(Objects.nonNull(condition.getUserId()), Tag::getUserId, condition.getUserId())
                .eq(loginUser.getRoleWeight() > 300, Tag::getUserId, loginUser.getUserId())
                .orderByDesc(Tag::getId));
        if (tagPage.getTotal() == 0)
            return new PagePojo<>();
        List<TagsBackDTO> tagsBackDTOList = BeanCopyUtil.copyList(tagPage.getRecords(), TagsBackDTO.class);
        List<Integer> userIdList = tagsBackDTOList.stream().map(TagsBackDTO::getUserId).distinct().collect(Collectors.toList());
        List<UserAuth> userAuthList = userAuthMapper.selectList(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUserId, UserAuth::getUsername)
                .in(UserAuth::getUserId, userIdList));
        Map<Integer, String> userAuthMap = userAuthList.stream().collect(Collectors.toMap(UserAuth::getUserId, UserAuth::getUsername, (k1, k2) -> k2));
        tagsBackDTOList.forEach(t -> t.setUsername(userAuthMap.get(t.getUserId())));
        return new PagePojo<>((int) tagPage.getTotal(), tagsBackDTOList);
    }

    @Override
    @Transactional
    public void deleteTagIdList(List<Integer> tagIdList) {
        if (CollectionUtils.isEmpty(tagIdList))
            throw new IllegalRequestException();
        LoginUser loginUser = UserUtil.getLoginUser();
        if (loginUser.getRoleWeight() > 300) {
            List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getId)
                    .eq(Tag::getUserId, loginUser.getUserId()));
            if (!tagList.stream().map(Tag::getId).collect(Collectors.toList()).containsAll(tagIdList))
                throw new IllegalRequestException();
        }
        int count = tagMapper.deleteTagIdList(tagIdList, UserUtil.getLoginUser().getUserId(), loginUser.getRoleWeight());
        if (count != tagIdList.size())
            throw new IllegalRequestException();
        articleTagMapper.deleteByTagIdList(tagIdList);
    }

    @Override
    @Transactional
    public void saveOrUpdateTagBackVO(TagBackVO tagBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        tagBackVO.setTagName(tagBackVO.getTagName().trim());
        if (Objects.isNull(tagBackVO.getId())) {
            Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                    .eq(Tag::getTagName, tagBackVO.getTagName())
                    .eq(Tag::getUserId, loginUser.getUserId()));
            if (count > 0)
                throw new OperationStatusException("标签名已存在!");
            Tag tag = BeanCopyUtil.copyObject(tagBackVO, Tag.class);
            tag.setUserId(loginUser.getUserId());
            tag.setCreateUser(loginUser.getUserId());
            tag.setCreateTime(new Date());
            tagMapper.insert(tag);
        } else {
            Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                    .eq(Tag::getTagName, tagBackVO.getTagName())
                    .eq(Tag::getUserId, loginUser.getUserId())
                    .ne(Tag::getId, tagBackVO.getId()));
            if (count > 0)
                throw new OperationStatusException("标签名已存在!");
            count = tagMapper.update(null, new LambdaUpdateWrapper<Tag>()
                    .set(Tag::getTagName, tagBackVO.getTagName())
                    .set(Tag::getUpdateUser, loginUser.getUserId())
                    .set(Tag::getUpdateTime, new Date())
                    .eq(Tag::getId, tagBackVO.getId())
                    .eq(loginUser.getRoleWeight() > 300, Tag::getUserId, loginUser.getUserId()));
            if (count != 1)
                throw new IllegalRequestException();
        }
    }
}




