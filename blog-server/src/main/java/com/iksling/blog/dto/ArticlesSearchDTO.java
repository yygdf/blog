package com.iksling.blog.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "article")
public class ArticlesSearchDTO {
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 用户id
     */
    @Field(type = FieldType.Integer)
    private Integer userId;

    /**
     * 用户名
     */
    @Field(type = FieldType.Text)
    private String username;

    /**
     * 文章标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String articleTitle;

    /**
     * 文章内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String articleContent;

    /**
     * 0不是草稿, 1是草稿
     */
    @Field(type = FieldType.Boolean)
    private Boolean draftFlag;

    /**
     * 0未公开, 1已公开
     */
    @Field(type = FieldType.Boolean)
    private Boolean publicFlag;

    /**
     * 0未隐藏, 1已隐藏
     */
    @Field(type = FieldType.Boolean)
    private Boolean hiddenFlag;
}
