package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.CommentService;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.OptLogConst.REMOVE;
import static com.iksling.blog.constant.OptLogConst.UPDATE;

@RestController
@Api(tags = "评论模块")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "查看后台评论列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/comments")
    public Result listBackComments(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(commentService.getPageCommentsBackDTO(condition));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新评论状态")
    @ApiImplicitParam(name = "updateBatchVO", value = "批量更新VO", required = true, dataType = "UpdateBatchVO")
    @PutMapping("/back/comments")
    public Result updateCommentsStatus(@Valid UpdateBatchVO updateBatchVO) {
        commentService.updateCommentsStatus(updateBatchVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "批量删除评论")
    @ApiImplicitParam(name = "commentIdList", value = "评论idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/comments")
    public Result deleteBackComments(@RequestBody List<Integer> commentIdList) {
        commentService.deleteCommentIdList(commentIdList);
        return Result.success().message("操作成功");
    }
}
