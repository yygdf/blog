package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.MessageService;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.vo.StatusBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.iksling.blog.constant.LogConst.REMOVE;
import static com.iksling.blog.constant.LogConst.UPDATE;

@RestController
@Api(tags = "留言模块")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除留言")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/messages")
    public Result deleteBackMessages(@RequestBody List<Integer> idList) {
        messageService.deleteBackMessagesByIdList(idList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新留言状态")
    @ApiImplicitParam(name = "statusBackVO", value = "批量更新VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/messages/status")
    public Result updateBackMessagesStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        messageService.updateMessagesStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台留言列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/back/messages")
    public Result getBackMessages(@Valid Condition condition) {
        return Result.success().message("查询成功").data(messageService.getMessagesBackDTO(condition));
    }
}
