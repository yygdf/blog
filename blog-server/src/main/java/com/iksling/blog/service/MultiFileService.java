package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.MultiFilesBackDTO;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.vo.*;

import java.util.List;

/**
 *
 */
public interface MultiFileService extends IService<MultiFile> {

    void saveMultiFilesBackVO(MultiFilesBackVO multiFilesBackVO);

    void saveOrUpdateMultiFileBackVO(MultiFileBackVO multiFileBackVO);

    void saveOrUpdateMultiFileTokenBackVO(TokenBackVO tokenBackVO);

    void deleteBackMultiFilesByIdList(List<Integer> idList);

    void updateMultiFileStatusBackVO(StatusBackVO statusBackVO);

    void updateMultiFilesStatusBackVO(StatusBackVO statusBackVO);

    List<MultiFilesBackDTO> getMultiFilesBackDTO(Condition condition);

    Dict getMultiFileTokenById(Integer id);
}
