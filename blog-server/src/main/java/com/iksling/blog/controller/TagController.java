package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.TagService;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.TagBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.OptLogConst.REMOVE;
import static com.iksling.blog.constant.OptLogConst.SAVE_OR_UPDATE;

@RestController
@Api(tags = "标签模块")
public class TagController {
    @Autowired
    private TagService tagService;

    @ApiOperation(value = "查看后台标签列表")
    @GetMapping("/back/tags")
    public Result listBackTags(ConditionVO condition) {
        return Result.success().message("查询成功").data(tagService.getPageTagsBackDTO(condition));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "批量删除标签")
    @DeleteMapping("/back/tags")
    public Result deleteBackTags(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTagIdList(tagIdList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改标签")
    @PostMapping("/back/tag")
    public Result saveBackTag(@Valid @RequestBody TagBackVO tagBackVO) {
        tagService.saveOrUpdateTagBackVO(tagBackVO);
        return Result.success().message("操作成功");
    }
}
