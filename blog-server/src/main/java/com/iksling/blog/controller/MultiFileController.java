package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.vo.MultiFileArticleBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.OptLogConst.REMOVE;
import static com.iksling.blog.constant.OptLogConst.UPLOAD;

@RestController
@Api(tags = "文件模块")
public class MultiFileController {
    @Autowired
    private MultiFileService multiFileService;

    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @PostMapping("/back/article/image")
    public Result saveBackArticleImage(@Valid MultiFileArticleBackVO multiFileArticleBackVO) {
        String url = multiFileService.saveMultiFileArticleBackVO(multiFileArticleBackVO);
        return Result.success().message("上传成功").data(url);
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除文章图片")
    @ApiImplicitParam(name = "url", value = "文章图片地址", required = true, dataType = "String")
    @DeleteMapping("/back/article/image")
    public Result deleteBackArticleImage(@RequestBody String url) {
        multiFileService.deleteArticleImageByUrl(url);
        return Result.success().message("操作成功");
    }
}
