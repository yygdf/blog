package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.MultiFileBackVO;
import com.iksling.blog.vo.StatusBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.CommonConst.STATIC_RESOURCE_URL;
import static com.iksling.blog.constant.LogConst.SAVE_OR_UPDATE;
import static com.iksling.blog.constant.LogConst.UPDATE;

@RestController
@Api(tags = "文件模块")
public class MultiFileController {
    @Autowired
    private MultiFileService multiFileService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改文件")
    @ApiImplicitParam(name = "multiFileBackVO", value = "文件后台VO", required = true, dataType = "MultiFileBackVO")
    @PostMapping("/back/multiFile")
    public Result saveOrUpdateBackMultiFile(@Valid @RequestBody MultiFileBackVO multiFileBackVO) {
        multiFileService.saveOrUpdateMultiFileBackVO(multiFileBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文件状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/multiFile/status")
    public Result updateBackMultiFileStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        multiFileService.updateMultiFileStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台文件列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionBackVO")
    @GetMapping("/back/multiFiles")
    public Result getBackMultiFiles(@Valid ConditionBackVO condition) {
        return Result.success().message("查询成功").data(Dict.create()
                .set("dataList", multiFileService.getMultiFilesBackDTO(condition))
                .set("staticResourceUrl", STATIC_RESOURCE_URL));
    }
}
