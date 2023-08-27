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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.OptLogConst.*;

@RestController
@Api(tags = "文件模块")
public class MultiFileController {
    @Autowired
    private MultiFileService multiFileService;

    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "articleImageBackVO", value = "文章图片后台VO", required = true, dataType = "ArticleImageBackVO")
    @PostMapping("/back/article/image")
    public Result saveBackArticleImage(@Valid ArticleImageBackVO articleImageBackVO) {
        String url = multiFileService.saveArticleImageBackVO(articleImageBackVO);
        return Result.success().message("上传成功").data(url);
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新文章图片删除状态")
    @ApiImplicitParam(name = "url", value = "文章图片地址", required = true, dataType = "String")
    @PutMapping("/back/article/image")
    public Result updateBackArticleImage(@RequestBody String url) {
        multiFileService.updateArticleImageByUrl(url);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "上传用户头像")
    @ApiImplicitParam(name = "articleImageBackVO", value = "用户头像后台VO", required = true, dataType = "UserAvatarBackVO")
    @PostMapping("/back/user/avatar")
    public Result saveBackUserAvatar(@Valid UserAvatarBackVO userAvatarBackVO) {
        String url = multiFileService.saveUserAvatarBackVO(userAvatarBackVO);
        return Result.success().message("上传成功").data(url);
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新用户头像删除状态")
    @ApiImplicitParam(name = "url", value = "用户头像地址", required = true, dataType = "String")
    @PutMapping("/back/user/avatar")
    public Result updateBackUserAvatar(@RequestBody String url) {
        multiFileService.updateUserAvatarByUrl(url);
        return Result.success().message("操作成功");
    }
}
