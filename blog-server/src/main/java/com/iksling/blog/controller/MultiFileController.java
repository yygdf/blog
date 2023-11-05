package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.vo.ArticleImageBackVO;
import com.iksling.blog.vo.UserAvatarBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public Result saveBackArticleImage(@Valid @RequestBody ArticleImageBackVO articleImageBackVO) {
        return Result.success().message("上传成功").data(multiFileService.saveArticleImageBackVO(articleImageBackVO));
    }

    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "上传用户头像")
    @ApiImplicitParam(name = "userAvatarBackVO", value = "用户头像后台VO", required = true, dataType = "UserAvatarBackVO")
    @PostMapping("/back/user/avatar")
    public Result saveBackUserAvatar(@Valid @RequestBody UserAvatarBackVO userAvatarBackVO) {
        return Result.success().message("上传成功").data(multiFileService.saveUserAvatarBackVO(userAvatarBackVO));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新文章图片状态")
    @ApiImplicitParam(name = "fileNameList", value = "文件名list", required = true, dataType = "List<Long>")
    @PutMapping("/back/article/images")
    public Result updateBackArticleImages(@RequestBody List<Long> fileNameList) {
        multiFileService.updateBackArticleImagesByFileNameList(fileNameList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新用户头像状态")
    @ApiImplicitParam(name = "fileNameList", value = "文件名list", required = true, dataType = "List<Long>")
    @PutMapping("/back/user/avatars")
    public Result updateBackUserAvatars(@RequestBody List<Long> fileNameList) {
        multiFileService.updateBackUserAvatarsByFileNameList(fileNameList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像文件", required = true, dataType = "MultipartFile")
    @PostMapping("/user/avatar")
    public Result updateUserAvatar(@RequestBody MultipartFile file) {
        return Result.success().message("操作成功").data(multiFileService.updateUserAvatar(file));
    }
}
