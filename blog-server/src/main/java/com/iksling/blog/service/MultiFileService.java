package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.MultiFilesBackDTO;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.MultiFileBackVO;
import com.iksling.blog.vo.MultiFilesBackVO;
import com.iksling.blog.vo.StatusBackVO;

import java.util.List;

/**
 *
 */
public interface MultiFileService extends IService<MultiFile> {

    void saveMultiFilesBackVO(MultiFilesBackVO multiFilesBackVO);

    void saveOrUpdateMultiFileBackVO(MultiFileBackVO multiFileBackVO);

    void updateMultiFileStatusBackVO(StatusBackVO statusBackVO);

    void updateMultiFilesStatusBackVO(StatusBackVO statusBackVO);

    List<MultiFilesBackDTO> getMultiFilesBackDTO(ConditionBackVO condition);
}
