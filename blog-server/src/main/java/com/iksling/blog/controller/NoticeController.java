package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.NoticeService;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.vo.NoticeBackVO;
import com.iksling.blog.vo.StatusBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.LogConst.SAVE;
import static com.iksling.blog.constant.LogConst.UPDATE;

@RestController
@Api(tags = "通知模块")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @OptLog(optType = SAVE)
    @ApiOperation(value = "发送通知")
    @ApiImplicitParam(name = "noticeBackVO", value = "通知后台VO", required = true, dataType = "NoticeBackVO")
    @PostMapping("/back/notice")
    public Result saveBackNotice(@Valid @RequestBody NoticeBackVO noticeBackVO) {
        noticeService.saveNoticeBackVO(noticeBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0004"));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新通知状态")
    @ApiImplicitParam(name = "StatusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/notices/status")
    public Result updateBackNoticesStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        noticeService.updateNoticesStatusBackVO(statusBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新通知读取状态")
    @ApiImplicitParam(name = "StatusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/notices/status/read")
    public Result updateBackNoticesStatusRead(@Valid @RequestBody StatusBackVO statusBackVO) {
        noticeService.updateNoticesStatusRead(statusBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @ApiOperation(value = "查看后台通知")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/notices")
    public Result getBackNotices(@Valid Condition condition) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(noticeService.getBackNotices(condition));
    }

    @ApiOperation(value = "查看后台未读通知")
    @GetMapping("/back/notices/unread")
    public Result getBackNoticesUnread() {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(noticeService.getBackNoticesUnread());
    }
}
