package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.CommentService;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.vo.CommentVO;
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
@Api(tags = "评论模块")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除评论")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/comments")
    public Result deleteBackComments(@RequestBody List<Integer> idList) {
        commentService.deleteBackCommentsByIdList(idList);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新评论状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/comments/status")
    public Result updateBackCommentsStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        commentService.updateCommentsStatusBackVO(statusBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @ApiOperation(value = "查看后台评论列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/back/comments")
    public Result getBackComments(@Valid Condition condition) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(commentService.getCommentsBackDTO(condition));
    }

    /****************************************************************************************************/

    @OptLog(optType = SAVE)
    @ApiOperation(value = "添加评论")
    @ApiImplicitParam(name = "commentVO", value = "评论VO", required = true, dataType = "CommentVO")
    @PostMapping("/comment")
    public Result saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveCommentVO(commentVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = SAVE)
    @ApiOperation(value = "点赞评论")
    @ApiImplicitParam(name = "id", value = "评论id", required = true, dataType = "Integer")
    @PostMapping("/comment/like/{id}")
    public Result saveCommentLike(@PathVariable Integer id) {
        commentService.saveCommentLike(id);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @ApiOperation(value = "查看评论列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/comments")
    public Result getComments(@Valid Condition condition) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(commentService.getCommentsDTO(condition));
    }

    @ApiOperation(value = "查看评论回复列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/comments/reply")
    public Result getCommentsReply(@Valid  Condition condition) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(commentService.getCommentsReplyDTO(condition));
    }
}
