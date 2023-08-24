package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.ResourceService;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ResourceBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.OptLogConst.*;

@RestController
@Api(tags = "资源模块")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @ApiOperation(value = "查看后台资源列表")
    @ApiImplicitParam(name = "keywords", value = "资源名称关键字", dataType = "String")
    @GetMapping("/back/resources")
    public Result listBackResources(String keywords) {
        return Result.success().message("查询成功").data(resourceService.getResourcesBackDTO(keywords));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改资源状态")
    @ApiImplicitParam(name = "commonStatusVO", value = "通用状态VO", required = true, dataType = "CommonStatusVO")
    @PutMapping("/back/resource/status")
    public Result updateResourceStatus(@Valid @RequestBody CommonStatusVO commonStatusVO) {
        resourceService.updateResourceStatusVO(commonStatusVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除资源")
    @ApiImplicitParam(name = "id", value = "资源id", required = true, dataType = "String")
    @DeleteMapping("/back/resource")
    public Result deleteBackResource(@RequestBody String id) {
        resourceService.deleteResourceById(id);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改资源")
    @ApiImplicitParam(name = "resourceBackVO", value = "资源后台VO", required = true, dataType = "ResourceBackVO")
    @PostMapping("/back/resource")
    public Result saveOrUpdateBackResource(@Valid @RequestBody ResourceBackVO resourceBackVO) {
        resourceService.saveOrUpdateResourceBackVO(resourceBackVO);
        return Result.success().message("操作成功");
    }
}
