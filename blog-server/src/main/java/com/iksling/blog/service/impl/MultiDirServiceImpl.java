package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.MultiDir;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.mapper.MultiDirMapper;
import com.iksling.blog.mapper.MultiFileMapper;
import com.iksling.blog.service.MultiDirService;
import com.iksling.blog.util.MultiFileUtil;
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

    @Autowired
    private MultiFileMapper multiFileMapper;

    @Override
    public void saveArticleDirById(Integer id, Integer loginUserId, Date createTime) {
        List<Map<String, Object>> multiDirMap = multiDirMapper.selectMaps(new QueryWrapper<MultiDir>()
                .select("id")
                .eq("user_id", loginUserId)
                .eq("dir_path", IMG_ARTICLE.getMark()));
        multiDirMapper.insert(MultiDir.builder()
                .userId(loginUserId)
                .parentId((Integer) multiDirMap.get(0).get("id"))
                .dirPath(Long.valueOf(id))
                .dirDesc("{'articleId':"+id+"}")
                .dirName(id.toString())
                .deletableFlag(false)
                .createUser(loginUserId)
                .createTime(createTime)
                .build());
    }

    @Override
    public void updateArticleDirByIdList(List<Integer> idList, Integer loginUserId, Date updateTime) {
        idList.forEach(
                e -> {
                    List<Map<String, Object>> multiDirMap = multiDirMapper.selectMaps(new QueryWrapper<MultiDir>()
                            .select("id", "user_id")
                            .eq("dir_path", e));
                    long dirPathNew = IdWorker.getId();
                    multiDirMapper.update(null, new LambdaUpdateWrapper<MultiDir>()
                            .set(MultiDir::getDirPathNew, dirPathNew)
                            .set(MultiDir::getDeletedFlag, true)
                            .set(MultiDir::getUpdateUser, loginUserId)
                            .set(MultiDir::getUpdateTime, updateTime)
                            .eq(MultiDir::getId, multiDirMap.get(0).get("id")));
                    String uri = multiDirMap.get(0).get("user_id") + "/" + IMG_ARTICLE.getPath() + "/" + e;
                    MultiFileUtil.rename(uri, uri + "-" + dirPathNew + "-article-del");
                }
        );
    }

    @Override
    public void deleteArticleDirByIdList(List<Integer> idList) {
        idList.forEach(
                e -> {
                    List<Map<String, Object>> multiDirMap = multiDirMapper.selectMaps(new QueryWrapper<MultiDir>()
                            .select("id", "user_id", "dir_path_new")
                            .eq("dir_path", e));
                    multiDirMapper.deleteById((Integer) multiDirMap.get(0).get("id"));
                    multiFileMapper.delete(new LambdaUpdateWrapper<MultiFile>().eq(MultiFile::getMultiDirId, multiDirMap.get(0).get("id")));
                    MultiFileUtil.delete(multiDirMap.get(0).get("user_id") + "/" + IMG_ARTICLE.getPath() + "/" + e + "-" + multiDirMap.get(0).get("dir_path_new") + "-article-del");
                }
        );
    }
}




