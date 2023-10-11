package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.MultiDir;
import com.iksling.blog.mapper.MultiDirMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MultiDirService;
import com.iksling.blog.util.MultiFileUtil;
import com.iksling.blog.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.iksling.blog.enums.FilePathEnum.IMG_ARTICLE;

/**
 *
 */
@Service
public class MultiDirServiceImpl extends ServiceImpl<MultiDirMapper, MultiDir>
    implements MultiDirService{
    @Autowired
    private MultiDirMapper multiDirMapper;

    @Override
    public void saveArticleDir(Integer id, String articleCover) {
        LoginUser loginUser = UserUtil.getLoginUser();
        List<Map<String, Object>> multiDirMap = multiDirMapper.selectMaps(new QueryWrapper<MultiDir>()
                .select("id")
                .eq("user_id", loginUser.getUserId())
                .eq("dir_path", IMG_ARTICLE.getMark()));
        multiDirMapper.insert(MultiDir.builder()
                .userId(loginUser.getUserId())
                .parentId((Integer) multiDirMap.get(0).get("id"))
                .dirPath(Long.valueOf(id))
                .dirDesc("文章[id:" + id + "]")
                .dirName(id.toString())
                .dirCover(articleCover)
                .deletableFlag(false)
                .createUser(loginUser.getUserId())
                .createTime(new Date())
                .build());
    }

    @Override
    public void updateArticleDirByIdList(List<Integer> idList) {
        idList.forEach(
                item -> {
                    List<Map<String, Object>> multiDirMap = multiDirMapper.selectMaps(new QueryWrapper<MultiDir>()
                            .select("id", "user_id")
                            .eq("dir_path", item));
                    long dirPathNew = IdWorker.getId();
                    multiDirMapper.update(null, new LambdaUpdateWrapper<MultiDir>()
                            .set(MultiDir::getDirPathNew, dirPathNew)
                            .set(MultiDir::getDeletedFlag, true)
                            .set(MultiDir::getUpdateUser, UserUtil.getLoginUser().getUserId())
                            .set(MultiDir::getUpdateTime, new Date())
                            .eq(MultiDir::getId, multiDirMap.get(0).get("id")));
                    String uri = multiDirMap.get(0).get("user_id") + "/" + IMG_ARTICLE.getPath() + "/" + item;
                    MultiFileUtil.rename(uri, uri + "-" + dirPathNew);
                }
        );
    }

    @Override
    public void removeArticleDirByIdList(List<Integer> idList) {
        idList.forEach(
                item -> {
                    List<Map<String, Object>> multiDirMap = multiDirMapper.selectMaps(new QueryWrapper<MultiDir>()
                            .select("id", "user_id")
                            .eq("dir_path", item));
                    multiDirMapper.deleteById((Integer) multiDirMap.get(0).get("id"));
                    MultiFileUtil.delete(multiDirMap.get(0).get("user_id") + "/" + IMG_ARTICLE.getPath() + "/" + item);
                }
        );
    }
}




