package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.ResourceService;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.vo.ResourceBackVO;
import com.iksling.blog.vo.StatusBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.LogConst.*;

@RestController
@Api(tags = "资源模块")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改资源")
    @ApiImplicitParam(name = "resourceBackVO", value = "资源后台VO", required = true, dataType = "ResourceBackVO")
    @PostMapping("/back/resource")
    public Result saveOrUpdateBackResource(@Valid @RequestBody ResourceBackVO resourceBackVO) {
        resourceService.saveOrUpdateResourceBackVO(resourceBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除资源")
    @ApiImplicitParam(name = "idList", value = "资源idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/resources")
    public Result deleteBackResources(@RequestBody List<Integer> idList) {
        resourceService.deleteBackResourcesByIdList(idList);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改资源状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/resource/status")
    public Result updateResourceStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        resourceService.updateResourceStatusBackVO(statusBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @ApiOperation(value = "查看后台资源列表")
    @ApiImplicitParam(name = "keywords", value = "关键字(资源名称)", dataType = "String")
    @GetMapping("/back/resources")
    public Result getBackResources(String keywords) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(resourceService.getResourcesBackDTO(keywords));
    }

    @ApiOperation(value = "查看资源模块名")
    @GetMapping("/back/resource/moduleNames")
    public Result getBackResourceModuleNames() {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(resourceService.getBackResourceModuleNames());
    }
}
