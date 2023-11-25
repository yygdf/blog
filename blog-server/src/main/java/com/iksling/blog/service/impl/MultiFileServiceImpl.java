package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.MultiFilesBackDTO;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.MultiFileMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.util.*;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.MultiFileBackVO;
import com.iksling.blog.vo.MultiFilesBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.STATIC_RESOURCE_URL;
import static com.iksling.blog.constant.FlagConst.*;
import static com.iksling.blog.enums.FileDirEnum.OTHER;
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
    public void saveMultiFilesBackVO(MultiFilesBackVO multiFilesBackVO) {
        List<MultipartFile> fileList = multiFilesBackVO.getFileList();
        multiFilesBackVO.setFileList(null);
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer multiFileUserId = multiFilesBackVO.getUserId();
        boolean checkSizeFlag = true;
        if (multiFileUserId == null)
            multiFileUserId = loginUser.getUserId();
        else if (loginUser.getRoleWeight() > 200 && !loginUser.getUserId().equals(multiFileUserId))
            throw new OperationStatusException();
        List<Object> objectList = multiFileMapper.selectObjs(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getFileFullPath)
                .eq(MultiFile::getId, multiFilesBackVO.getId())
                .eq(MultiFile::getUserId, multiFileUserId)
                .eq(MultiFile::getFileMark, 0)
                .eq(MultiFile::getDeletedFlag, false)
                .eq(MultiFile::getDeletableFlag, true));
        if (objectList.isEmpty())
            throw new OperationStatusException();
        List<MultiFile> multiFileList = new ArrayList<>();
        if (loginUser.getRoleWeight() <= 200)
            checkSizeFlag = false;
        Date createTime = new Date();
        for (MultipartFile file : fileList) {
            MultiFileUtil.checkValidFile(file, OTHER, checkSizeFlag);
            long fileName = IdWorker.getId();
            String[] originalFilenameArr = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
            String targetAddr = objectList.get(0).toString();
            String fullFileName = fileName + "." + originalFilenameArr[1];
            String url = MultiFileUtil.upload(file, targetAddr, fullFileName);
            if (url == null)
                throw new FileStatusException("文件上传失败!");
            String iPAddress = IpUtil.getIpAddress(request);
            multiFileList.add(MultiFile.builder()
                    .userId(multiFileUserId)
                    .parentId(multiFilesBackVO.getId())
                    .fileDesc("{'userId':"+multiFileUserId+",'multiFileId':"+multiFilesBackVO.getId()+"}")
                    .fileMark(OTHER.getCurrentPath().intValue())
                    .fileName(fileName)
                    .fileSize(file.getSize())
                    .fileFullPath(targetAddr + "/" + fullFileName)
                    .fileExtension(originalFilenameArr[1])
                    .fileNameOrigin(originalFilenameArr[0])
                    .ipSource(IpUtil.getIpSource(iPAddress))
                    .ipAddress(iPAddress)
                    .createUser(loginUser.getUserId())
                    .createTime(createTime)
                    .build());
        }
        this.saveBatch(multiFileList);
    }

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
            if (multiFile.getParentId() == null)
                multiFile.setFileFullPath(loginUserId + "/" + fileName);
            else {
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
                .and(loginUser.getRoleWeight() > 200, e -> e.eq(MultiFile::getUserId, loginUserId).eq(MultiFile::getDeletableFlag, true))
                .eq(MultiFile::getDeletedFlag, false));
        if (mapList.size() != statusBackVO.getIdList().size())
            throw new OperationStatusException();
        if (HIDDEN.equals(statusBackVO.getType())) {
            mapList.forEach(e -> {
                long fileNameNew = IdWorker.getId();
                String fileFullPath = e.get("file_full_path").toString();
                String fileFullName = getSplitStringByIndex(fileFullPath, "/", -1);
                String fileFullPathOld = fileFullPath.substring(0, fileFullPath.length() - fileFullName.length()) + getSplitStringByIndex(fileFullName, "[_.]", 0);
                String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_";
                if (e.get("hidden_flag").equals(false)) {
                    fileFullPathNew += HIDDEN;
                    if (e.get("file_mark").equals(0)) {
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileNameNew, fileNameNew)
                                .set(MultiFile::getHiddenFlag, true)
                                .eq(MultiFile::getId, e.get("id")));
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .setSql("file_full_path=replace(file_full_path,'"+fileFullPath+"','"+fileFullPathNew+"')")
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
                    fileFullPathNew += PUBLIC;
                    if (e.get("file_mark").equals(0)) {
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileNameNew, fileNameNew)
                                .set(MultiFile::getHiddenFlag, false)
                                .eq(MultiFile::getId, e.get("id")));
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .setSql("file_full_path=replace(file_full_path,'"+fileFullPath+"','"+fileFullPathNew+"')")
                                .likeRight(MultiFile::getFileFullPath, fileFullPath));
                    } else {
                        fileFullPathNew += "." + e.get("file_extension");
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
                                .setSql("file_full_path=replace(file_full_path,'"+fileFullPath+"','"+fileFullPathNew+"')")
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
                    String fileFullName = getSplitStringByIndex(fileFullPath, "/", -1);
                    String fileFullPathOld = fileFullPath.substring(0, fileFullPath.length() - fileFullName.length()) + getSplitStringByIndex(fileFullName, "[_.]", 0);
                    String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + PUBLIC;
                    if (e.get("public_flag").equals(true)) {
                        if (e.get("file_mark").equals(0)) {
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .set(MultiFile::getFileNameNew, fileNameNew)
                                    .set(MultiFile::getPublicFlag, false)
                                    .eq(MultiFile::getId, e.get("id")));
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .setSql("file_full_path=replace(file_full_path,'"+fileFullPath+"','"+fileFullPathNew+"')")
                                    .likeRight(MultiFile::getFileFullPath, fileFullPath));
                        } else {
                            fileFullPathNew += "." + e.get("file_extension");
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
                                    .setSql("file_full_path=replace(file_full_path,'"+fileFullPath+"','"+fileFullPathNew+"')")
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
                            .setSql("public_flag = !public_flag")
                            .eq(MultiFile::getId, e.get("id")));
                }
            });
        }
    }

    @Override
    public List<MultiFilesBackDTO> getMultiFilesBackDTO(ConditionBackVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer userId = condition.getUserId();
        LambdaQueryWrapper<MultiFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (DELETED.equals(condition.getType())) {
            if (loginUser.getRoleWeight() > 100)
                return new ArrayList<>();
            lambdaQueryWrapper.eq(MultiFile::getDeletedFlag, true);
        } else
            lambdaQueryWrapper.eq(MultiFile::getDeletedFlag, false);
        if (loginUser.getRoleWeight() > 200) {
            userId = loginUser.getUserId();
            lambdaQueryWrapper.eq(MultiFile::getDeletableFlag, true);
        } else if (userId == null)
            userId = loginUser.getUserId();
        lambdaQueryWrapper.eq(MultiFile::getUserId, userId);
        if (CommonUtil.isNotEmpty(condition.getKeywords())) {
            if (condition.getFlag() == Boolean.TRUE) {
                if (condition.getCategoryId() != null) {
                    List<Object> objectList = multiFileMapper.selectObjs(new LambdaQueryWrapper<MultiFile>()
                            .select(MultiFile::getFileFullPath)
                            .eq(MultiFile::getId, condition.getCategoryId())
                            .eq(MultiFile::getUserId, userId));
                    if (objectList.isEmpty())
                        return new ArrayList<>();
                    lambdaQueryWrapper.likeRight(MultiFile::getFileFullPath, objectList.get(0));
                } else
                    lambdaQueryWrapper.likeRight(MultiFile::getFileFullPath, userId);
            } else
                lambdaQueryWrapper.eq(MultiFile::getParentId, condition.getCategoryId() == null ? -1 : condition.getCategoryId());
            lambdaQueryWrapper.like(MultiFile::getFileNameOrigin, condition.getKeywords());
        } else
            lambdaQueryWrapper.eq(MultiFile::getParentId, condition.getCategoryId() == null ? -1 : condition.getCategoryId());
        List<MultiFile> multiFileList = multiFileMapper.selectList(lambdaQueryWrapper
                .select(MultiFile::getId, MultiFile::getUserId, MultiFile::getParentId,
                        MultiFile::getFileDesc, MultiFile::getFileSize, MultiFile::getFileCover,
                        MultiFile::getFileFullPath, MultiFile::getFileExtension, MultiFile::getFileNameOrigin,
                        MultiFile::getPublicFlag, MultiFile::getHiddenFlag, MultiFile::getDeletableFlag,
                        MultiFile::getCreateTime, MultiFile::getUpdateTime)
                .orderByDesc(MultiFile::getFileMark)
                .orderByAsc(MultiFile::getFileNameOrigin));
        return  multiFileList.stream()
                .map(e -> {
                    MultiFilesBackDTO multiFilesBackDTO = BeanCopyUtil.copyObject(e, MultiFilesBackDTO.class);
                    multiFilesBackDTO.setHasChildren(multiFilesBackDTO.getFileExtension().length() == 0);
                    return multiFilesBackDTO;
                })
                .collect(Collectors.toList());
    }
}




