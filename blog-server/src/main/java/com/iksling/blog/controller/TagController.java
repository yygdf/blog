package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.TagService;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.TagBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.LogConst.*;

@RestController
@Api(tags = "标签模块")
public class TagController {
    @Autowired
    private TagService tagService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改标签")
    @ApiImplicitParam(name = "tagBackVO", value = "标签后台VO", required = true, dataType = "TagBackVO")
    @PostMapping("/back/tag")
    public Result saveOrUpdateBackTag(@Valid @RequestBody TagBackVO tagBackVO) {
        tagService.saveOrUpdateTagBackVO(tagBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除标签")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/tags")
    public Result deleteBackTags(@RequestBody List<Integer> idList) {
        tagService.deleteBackTagsByIdList(idList);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新标签状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/tags/status")
    public Result updateBackTagsStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        tagService.updateTagsStatusBackVO(statusBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @ApiOperation(value = "查看后台标签列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/back/tags")
    public Result getBackTags(@Valid Condition condition) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(tagService.getTagsBackDTO(condition));
    }

    /****************************************************************************************************/

    @ApiOperation(value = "查看标签列表")
    @GetMapping("/tags")
    public Result getTags() {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(tagService.getTagsDTO());
    }
}
