package com.iksling.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iksling.blog.dto.StatisticBackDTO;
import com.iksling.blog.entity.*;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.*;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.BlogService;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.util.RedisUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.TokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.*;
import static com.iksling.blog.constant.RedisConst.*;
import static com.iksling.blog.util.DateUtil.*;

/**
 *
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserConfigMapper userConfigMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    @Resource
    private HttpServletRequest request;

    @Autowired
    private UserConfigServiceImpl userConfigServiceImpl;

    @Override
    @Transactional
    public void updateBackAbout(String aboutContent) {
        Object o = JSON.parseObject(aboutContent, Map.class).get("aboutContent");
        if (o != null)
            RedisUtil.setMapValue(BLOG_ABOUT_ME, UserUtil.getLoginUser().getUserId().toString().trim(), o);
    }

    @Override
    @Transactional
    public void updateBackBlogMessageConfig(StatusBackVO statusBackVO) {
        Integer type = statusBackVO.getIdList().get(0);
        if (type < 1 || type > 4)
            throw new OperationStatusException();
        Integer value = statusBackVO.getType();
        if (value < 1 || value > 2)
            throw new OperationStatusException();
        String loginUserId = UserUtil.getLoginUser().getUserId().toString();
        HashMap<String, Integer> map = UserUtil.getUserMessageConfig(loginUserId);
        map.put(type.toString(), value);
        USER_MESSAGE_CONFIG_MAP.put(loginUserId, map);
        RedisUtil.setMapValue(USER_MESSAGE_CONFIG, loginUserId, map);
    }

    @Override
    public HashMap<String, Integer> getBackBlogMessageConfig() {
        return UserUtil.getUserMessageConfig(UserUtil.getLoginUser().getUserId());
    }

    @Override
    public Dict getBackArticleStatistic(Integer userId, Date endDate, Integer days) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (loginUser.getRoleWeight() > 300)
            userId = loginUser.getUserId();
        if (days == null)
            days = 7;
        if (endDate == null)
            endDate = new Date();
        Dict dict = Dict.create();
        List<StatisticBackDTO> viewCountDTOList = new ArrayList<>();
        Map<String, Integer> viewCountMap;
        Map<String, Integer> likeCountMap;
        if (userId == null) {
            while (--days > -1) {
                String name = dateToStr(getSomeDay(endDate, -days), YYYY_MM_DD);
                viewCountDTOList.add(StatisticBackDTO.builder()
                        .name(name)
                        .value(RedisUtil.getMap(name + "_avc").values().stream().mapToInt(e -> (int) e).sum()).build());
            }
            viewCountMap = RedisUtil.getMaps(ARTICLE_VIEW_COUNT + "_*");
            likeCountMap = RedisUtil.getMaps(ARTICLE_LIKE_COUNT + "_*");
        } else {
            while (--days > -1) {
                String name = dateToStr(getSomeDay(endDate, -days), YYYY_MM_DD);
                viewCountDTOList.add(StatisticBackDTO.builder()
                        .name(name)
                        .value(RedisUtil.getMapValue( name + "_avc", userId.toString())).build());
            }
            viewCountMap = RedisUtil.getMap(ARTICLE_VIEW_COUNT + "_" + userId);
            likeCountMap = RedisUtil.getMap(ARTICLE_LIKE_COUNT + "_" + userId);
        }
        dict.set("viewCountDTOList", viewCountDTOList);
        dict.set("viewCountRankDTOList", getArticleNameMap(viewCountMap));
        dict.set("likeCountRankDTOList", getArticleNameMap(likeCountMap));
        return dict;
    }

    @Override
    @Transactional
    public Object saveTokenVO(TokenVO tokenVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        Integer id = tokenVO.getId();
        if (tokenVO.getType() == null) {
            Map<String, Object> map = RedisUtil.getMap(ARTICLE_TOKEN + "_" + id);
            if (!tokenVO.getAccessToken().equals(map.get("accessToken")))
                throw new OperationStatusException(LocaleUtil.getMessage("S0003"));
            Integer count = (Integer) map.get("effectiveCount");
            if (count == 0)
                throw new OperationStatusException(LocaleUtil.getMessage("S0004"));
            HashSet<Integer> articleTokenSet = RedisUtil.getMapValue(ARTICLE_TOKEN, loginUserId.toString());
            if (articleTokenSet == null)
                articleTokenSet = new HashSet<>();
            if (articleTokenSet.contains(id))
                return null;
            articleTokenSet.add(id);
            RedisUtil.setMapValue(ARTICLE_TOKEN, loginUserId.toString(), articleTokenSet);
            if (count != -1)
                RedisUtil.increment(ARTICLE_TOKEN + "_" + id, "effectiveCount", -1);
            return articleMapper.selectObjs(new LambdaQueryWrapper<Article>().select(Article::getArticleContent).eq(Article::getId, id)).get(0);
        }
        return null;
    }

    @Override
    public String getAbout() {
        String bloggerId = request.getHeader("Blogger-Id");
        if (bloggerId != null) {
            String aboutContent = RedisUtil.getMapValue(BLOG_ABOUT_ME, bloggerId);
            return aboutContent == null ? "" : aboutContent;
        }
        String aboutContent = RedisUtil.getMapValue(BLOG_ABOUT_ME, UserUtil.getLoginUser().getUserId().toString());
        return aboutContent == null ? "" : aboutContent;
    }

    @Override
    public Integer getBlogId(Integer bloggerId) {
        if (bloggerId == null)
            return HOME_BLOGGER_ID;
        Integer count = userAuthMapper.selectCount(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUserId, bloggerId)
                .eq(UserAuth::getDeletedFlag, false)
                .eq(UserAuth::getAssimilateNowFlag, true));
        if (count == 0)
            return HOME_BLOGGER_ID;
        return bloggerId;
    }

    @Override
    public HashMap<String, Object> getBlogInfo() {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap<String, Object> blogConfigMap;
        HashMap<String, Object> bloggerInfoMap = new HashMap<>();
        Integer bloggerId = Integer.valueOf(request.getHeader("Blogger-Id"));
        if (bloggerId.equals(ROOT_USER_ID) || !ENABLE_USER_CONFIG) {
            blogConfigMap = new HashMap<>(userConfigServiceImpl.getUserConfigMap());
        } else {
            blogConfigMap = userConfigMapper.selectList(new LambdaQueryWrapper<UserConfig>()
                    .select(UserConfig::getConfigName, UserConfig::getConfigValue)
                    .eq(UserConfig::getUserId, bloggerId)
                    .eq(UserConfig::getDeletedFlag, false))
                    .stream()
                    .collect(Collectors.toMap(UserConfig::getConfigName, UserConfig::getConfigValue, (key1, key2) -> key2, HashMap::new));
        }
        User blogger = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getAvatar, User::getNickname, User::getIntro)
                .eq(User::getId, bloggerId));
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getDraftFlag, false)
                .eq(Article::getRecycleFlag, false)
                .eq(Article::getDeletedFlag, false)
                .eq(Article::getUserId, bloggerId));
        Integer categoryCount = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .eq(Category::getDeletedFlag, false)
                .eq(Category::getUserId, bloggerId));
        Integer tagCount = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getDeletedFlag, false)
                .eq(Tag::getUserId, bloggerId));
        Integer viewCount = RedisUtil.getMapValue(BLOG_VIEW_COUNT, bloggerId.toString());
        bloggerInfoMap.put("avatar", blogger.getAvatar());
        bloggerInfoMap.put("nickname", blogger.getNickname());
        bloggerInfoMap.put("intro", blogger.getIntro());
        bloggerInfoMap.put("articleCount", articleCount);
        bloggerInfoMap.put("categoryCount", categoryCount);
        bloggerInfoMap.put("tagCount", tagCount);
        bloggerInfoMap.put("viewCount", viewCount);
        hashMap.put("bloggerId", bloggerId);
        hashMap.put("blogConfig", blogConfigMap);
        hashMap.put("bloggerInfo", bloggerInfoMap);
        return hashMap;
    }

    private List<StatisticBackDTO> getArticleNameMap(Map<String, Integer> map) {
        List<StatisticBackDTO> list = new ArrayList<>();
        List<Integer> articleIdList = map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(item -> Integer.valueOf(item.getKey()))
                .limit(10)
                .collect(Collectors.toList());
        if (articleIdList.size() != 0) {
            Map<Integer, String> viewNameMap = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                    .select(Article::getId, Article::getArticleTitle)
                    .in(Article::getId, articleIdList)
                    .eq(Article::getDeletedFlag, false)).stream()
                    .collect(Collectors.toMap(Article::getId, Article::getArticleTitle, (key1, key2) -> key2, HashMap::new));
            articleIdList.forEach(e -> {
                if (viewNameMap.get(e) != null) {
                    list.add(StatisticBackDTO.builder()
                            .name(viewNameMap.get(e))
                            .value(map.get(e.toString()))
                            .build());
                }
            });
        }
        return list;
    }
}




