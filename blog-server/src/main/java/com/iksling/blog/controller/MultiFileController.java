package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.vo.ArticleImgBackVO;
import com.iksling.blog.vo.UserAvatarBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.LogConst.UPDATE;
import static com.iksling.blog.constant.LogConst.UPLOAD;

@RestController
@Api(tags = "文件模块")
public class MultiFileController {
    @Autowired
    private MultiFileService multiFileService;

    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "articleImageBackVO", value = "文章图片后台VO", required = true, dataType = "ArticleImageBackVO")
    @PostMapping("/back/article/image")
    public Result saveBackArticleImage(@Valid ArticleImgBackVO articleImgBackVO) {
        String url = multiFileService.saveArticleImgBackVO(articleImgBackVO);
        return Result.success().message("上传成功").data(url);
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新文章图片状态")
    @ApiImplicitParam(name = "fileNameList", value = "文件名list", required = true, dataType = "List<Long>")
    @PutMapping("/back/article/image")
    public Result updateBackArticleImages(@RequestBody List<Long> fileNameList) {
        multiFileService.updateArticleImgByFileNameList(fileNameList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "上传用户头像")
    @ApiImplicitParam(name = "userAvatarBackVO", value = "用户头像后台VO", required = true, dataType = "UserAvatarBackVO")
    @PostMapping("/back/user/avatar")
    public Result saveBackUserAvatar(@Valid UserAvatarBackVO userAvatarBackVO) {
        String url = multiFileService.saveUserAvatarBackVO(userAvatarBackVO);
        return Result.success().message("上传成功").data(url);
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新用户头像状态")
    @ApiImplicitParam(name = "fileName", value = "用户头像名", required = true, dataType = "Long")
    @PutMapping("/back/user/avatar")
    public Result updateBackUserAvatar(@RequestParam Long fileName) {
        multiFileService.updateUserAvatarByFileName(fileName);
        return Result.success().message("操作成功");
    }
}
