package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "评论VO")
public class CommentVO {
    /**
     * 回复用户id
     */
    @ApiModelProperty(name = "replyId", value = "回复用户id", dataType = "Integer")
    private Integer replyId;

    /**
     * 父评论id
     */
    @ApiModelProperty(name = "parentId", value = "父评论id", dataType = "Integer")
    private Integer parentId;

    /**
     * 文章id
     */
    @ApiModelProperty(name = "articleId", value = "文章id", dataType = "Integer")
    private Integer articleId;

    /**
     * 评论内容
     */
    @NotNull(message = "'commentContent':'NotNull'")
    @Size(min = 1, message = "'commentContent':{'minlength':1}")
    @ApiModelProperty(name = "commentContent", value = "评论内容", dataType = "String")
    private String commentContent;
}
