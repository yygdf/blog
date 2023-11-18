package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.MultiFilesBackDTO;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.MultiFileMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.util.*;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.MultiFileBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.STATIC_RESOURCE_URL;
import static com.iksling.blog.constant.FlagConst.*;
import static com.iksling.blog.util.CommonUtil.getSplitStringByIndex;

/**
 *
 */
@Service
public class MultiFileServiceImpl extends ServiceImpl<MultiFileMapper, MultiFile>
    implements MultiFileService{
    @Autowired
    private MultiFileMapper multiFileMapper;

    @Resource
    private HttpServletRequest request;

    @Override
    @Transactional
    public void saveOrUpdateMultiFileBackVO(MultiFileBackVO multiFileBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        MultiFile multiFile = BeanCopyUtil.copyObject(multiFileBackVO, MultiFile.class);
        if (multiFile.getId() == null) {
            if (multiFile.getFileNameOrigin() == null)
                throw new OperationStatusException();
            long fileName = IdWorker.getId();
            if (multiFile.getParentId() == null) {
                multiFile.setFileFullPath(loginUserId + "/" + fileName);
            } else {
                List<Object> objectList = multiFileMapper.selectObjs(new LambdaQueryWrapper<MultiFile>()
                        .select(MultiFile::getFileFullPath)
                        .eq(MultiFile::getId, multiFile.getParentId())
                        .eq(MultiFile::getUserId, loginUserId)
                        .eq(MultiFile::getFileMark, 0)
                        .eq(MultiFile::getDeletedFlag, false)
                        .eq(MultiFile::getDeletableFlag, true));
                if (objectList.isEmpty())
                    throw new OperationStatusException();
                multiFile.setFileFullPath(objectList.get(0) + "/" + fileName);
            }
            if (CommonUtil.isNotEmpty(multiFile.getFileCover()) && !multiFile.getFileCover().startsWith(STATIC_RESOURCE_URL))
                multiFile.setFileCover(null);
            String iPAddress = IpUtil.getIpAddress(request);
            multiFile.setUserId(loginUserId);
            multiFile.setFileName(fileName);
            multiFile.setIpSource(IpUtil.getIpSource(iPAddress));
            multiFile.setIpAddress(iPAddress);
            multiFile.setCreateUser(loginUserId);
            multiFile.setCreateTime(new Date());
            multiFileMapper.insert(multiFile);
        } else {
            Integer count = multiFileMapper.selectCount(new LambdaQueryWrapper<MultiFile>()
                    .eq(MultiFile::getId, multiFile.getId())
                    .eq(loginUser.getRoleWeight() > 200, MultiFile::getUserId, loginUserId)
                    .eq(MultiFile::getDeletedFlag, false)
                    .eq(MultiFile::getDeletableFlag, true));
            if (count != 1)
                throw new OperationStatusException();
            if (CommonUtil.isNotEmpty(multiFile.getFileCover()) && !multiFile.getFileCover().startsWith(STATIC_RESOURCE_URL))
                multiFile.setFileCover("");
            multiFile.setUpdateUser(loginUserId);
            multiFile.setUpdateTime(new Date());
            multiFileMapper.updateById(multiFile);
        }
    }

    @Override
    @Transactional
    public void updateMultiFileStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileMark, MultiFile::getFileFullPath, MultiFile::getFileExtension, MultiFile::getPublicFlag, MultiFile::getHiddenFlag)
                .in(MultiFile::getId, statusBackVO.getIdList())
                .eq(loginUser.getRoleWeight() > 200, MultiFile::getUserId, loginUserId)
                .eq(MultiFile::getDeletedFlag, false)
                .eq(MultiFile::getDeletableFlag, true));
        if (mapList.size() != statusBackVO.getIdList().size())
            throw new OperationStatusException();
        if (HIDDEN.equals(statusBackVO.getType())) {
            mapList.forEach(e -> {
                long fileNameNew = IdWorker.getId();
                String fileFullPath = e.get("file_full_path").toString();
                String fileFullPathOld = getSplitStringByIndex(fileFullPath, "[_.]", 0);
                String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_";
                if (e.get("hidden_flag").equals(false)) {
                    fileFullPathNew += fileFullPathNew  + HIDDEN;
                    if (e.get("file_mark").equals(0)) {
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileNameNew, fileNameNew)
                                .set(MultiFile::getHiddenFlag, true)
                                .eq(MultiFile::getId, e.get("id")));
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .setSql("file_full_path=replace(file_full_path,"+fileFullPath+","+fileFullPathNew+")")
                                .likeRight(MultiFile::getFileFullPath, fileFullPath));
                    } else {
                        fileFullPathNew += "." + e.get("file_extension");
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileFullPath, fileFullPathNew)
                                .set(MultiFile::getFileNameNew, fileNameNew)
                                .set(MultiFile::getHiddenFlag, true)
                                .eq(MultiFile::getId, e.get("id")));
                    }
                } else if (e.get("public_flag").equals(false)) {
                    fileFullPathNew += fileFullPathNew + PUBLIC;
                    if (e.get("file_mark").equals(0)) {
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileNameNew, fileNameNew)
                                .set(MultiFile::getHiddenFlag, false)
                                .eq(MultiFile::getId, e.get("id")));
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .setSql("file_full_path=replace(file_full_path,"+fileFullPath+","+fileFullPathNew+")")
                                .likeRight(MultiFile::getFileFullPath, fileFullPath));
                    } else {
                        fileFullPathNew += fileFullPathNew + "." + e.get("file_extension");
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileFullPath, fileFullPathNew)
                                .set(MultiFile::getFileNameNew, fileNameNew)
                                .set(MultiFile::getHiddenFlag, false)
                                .eq(MultiFile::getId, e.get("id")));
                    }
                } else {
                    if (e.get("file_mark").equals(0)) {
                        fileFullPathNew = fileFullPathOld;
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getHiddenFlag, false)
                                .eq(MultiFile::getId, e.get("id")));
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .setSql("file_full_path=replace(file_full_path,"+fileFullPath+","+fileFullPathNew+")")
                                .likeRight(MultiFile::getFileFullPath, fileFullPath));
                    } else {
                        fileFullPathNew = fileFullPathOld + "." + e.get("file_extension");
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileFullPath, fileFullPathNew)
                                .set(MultiFile::getHiddenFlag, false)
                                .eq(MultiFile::getId, e.get("id")));
                    }
                }
                MultiFileUtil.rename(fileFullPath, fileFullPathNew);
            });
        } else {
            mapList.forEach(e -> {
                if (e.get("hidden_flag").equals(false)) {
                    long fileNameNew = IdWorker.getId();
                    String fileFullPath = e.get("file_full_path").toString();
                    String fileFullPathOld = getSplitStringByIndex(fileFullPath, "[_.]", 0);
                    String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + PUBLIC;
                    if (e.get("public_flag").equals(true)) {
                        if (e.get("file_mark").equals(0)) {
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .set(MultiFile::getFileNameNew, fileNameNew)
                                    .set(MultiFile::getPublicFlag, false)
                                    .eq(MultiFile::getId, e.get("id")));
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .setSql("file_full_path=replace(file_full_path,"+fileFullPath+","+fileFullPathNew+")")
                                    .likeRight(MultiFile::getFileFullPath, fileFullPath));
                        } else {
                            fileFullPathNew += fileFullPathNew + "." + e.get("file_extension");
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .set(MultiFile::getFileFullPath, fileFullPathNew)
                                    .set(MultiFile::getFileNameNew, fileNameNew)
                                    .set(MultiFile::getPublicFlag, false)
                                    .eq(MultiFile::getId, e.get("id")));
                        }
                    } else {
                        if (e.get("file_mark").equals(0)) {
                            fileFullPathNew = fileFullPathOld;
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .set(MultiFile::getPublicFlag, true)
                                    .eq(MultiFile::getId, e.get("id")));
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .setSql("file_full_path=replace(file_full_path,"+fileFullPath+","+fileFullPathNew+")")
                                    .likeRight(MultiFile::getFileFullPath, fileFullPath));
                        } else {
                            fileFullPathNew = fileFullPathOld + "." + e.get("file_extension");
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .set(MultiFile::getFileFullPath, fileFullPathNew)
                                    .set(MultiFile::getPublicFlag, true)
                                    .eq(MultiFile::getId, e.get("id")));
                        }
                    }
                    MultiFileUtil.rename(fileFullPath, fileFullPathNew);
                } else {
                    multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                            .setSql("public_flag=!public_flag")
                            .eq(MultiFile::getId, e.get("id")));
                }
            });
        }
    }

    @Override
    public List<MultiFilesBackDTO> getMultiFilesBackDTO(ConditionBackVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaQueryWrapper<MultiFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (DELETED.equals(condition.getType())) {
            if (loginUser.getRoleWeight() > 100)
                return new ArrayList<>();
            lambdaQueryWrapper.eq(MultiFile::getDeletedFlag, true);
        } else
            lambdaQueryWrapper.eq(MultiFile::getDeletedFlag, false);
        if (condition.getUserId() == null || loginUser.getRoleWeight() > 200)
            lambdaQueryWrapper.eq(MultiFile::getUserId, loginUser.getUserId());
        else
            lambdaQueryWrapper.eq(MultiFile::getUserId, condition.getUserId());
        if (CommonUtil.isEmpty(condition.getKeywords()))
            lambdaQueryWrapper.eq(MultiFile::getParentId, -1);
        else
            lambdaQueryWrapper.like(MultiFile::getFileNameOrigin, condition.getKeywords());
        List<MultiFile> multiFileList = multiFileMapper.selectList(lambdaQueryWrapper
                .select(MultiFile::getId, MultiFile::getUserId, MultiFile::getFileDesc,
                        MultiFile::getFileSize, MultiFile::getFileCover,
                        MultiFile::getFileFullPath, MultiFile::getFileExtension, MultiFile::getFileNameOrigin,
                        MultiFile::getPublicFlag, MultiFile::getHiddenFlag,MultiFile::getCreateTime,
                        MultiFile::getUpdateTime));
        return  multiFileList.stream()
                .map(e -> {
                    MultiFilesBackDTO multiFilesBackDTO = BeanCopyUtil.copyObject(e, MultiFilesBackDTO.class);
                    multiFilesBackDTO.setHasChildren(multiFilesBackDTO.getFileExtension().length() == 0);
                    return multiFilesBackDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MultiFilesBackDTO> getMultiFilesBackDTOById(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        List<MultiFile> multiFileList = multiFileMapper.selectList(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getUserId, MultiFile::getFileDesc,
                        MultiFile::getFileSize, MultiFile::getFileCover,
                        MultiFile::getFileFullPath, MultiFile::getFileExtension, MultiFile::getFileNameOrigin,
                        MultiFile::getPublicFlag, MultiFile::getHiddenFlag, MultiFile::getCreateTime,
                        MultiFile::getUpdateTime)
                .eq(loginUser.getRoleWeight() > 100, MultiFile::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 200, MultiFile::getUserId, loginUser.getUserId())
                .eq(MultiFile::getParentId, id));
        return  multiFileList.stream()
                .map(e -> {
                    MultiFilesBackDTO multiFilesBackDTO = BeanCopyUtil.copyObject(e, MultiFilesBackDTO.class);
                    multiFilesBackDTO.setHasChildren(multiFilesBackDTO.getFileExtension().length() == 0);
                    return multiFilesBackDTO;
                })
                .collect(Collectors.toList());
    }
}




