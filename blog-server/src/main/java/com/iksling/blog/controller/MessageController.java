package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.MessageService;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
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

    @ApiOperation(value = "查看后台留言列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/messages")
    public Result listBackMessages(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(messageService.getPageMessagesBackDTO(condition));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新留言状态")
    @ApiImplicitParam(name = "updateBatchVO", value = "批量更新VO", required = true, dataType = "UpdateBatchVO")
    @PutMapping("/back/messages")
    public Result updateMessagesStatus(@Valid UpdateBatchVO updateBatchVO) {
        messageService.updateMessagesStatus(updateBatchVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "批量删除留言")
    @ApiImplicitParam(name = "messageIdList", value = "留言idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/messages")
    public Result deleteBackMessages(@RequestBody List<Integer> messageIdList) {
        messageService.deleteMessageIdList(messageIdList);
        return Result.success().message("操作成功");
    }
}
