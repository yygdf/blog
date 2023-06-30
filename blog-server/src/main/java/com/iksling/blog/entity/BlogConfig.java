package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tb_blog_config
 */
@TableName(value ="tb_blog_config")
@Data
public class BlogConfig implements Serializable {
    /**
     * 配置id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 配置名
     */
    private String configName;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 配置描述
     */
    private String configDesc;

    /**
     * 0未删除，1已删除
     */
    private Boolean isDeleted;

    /**
     * 0可以删除，1不可以删除
     */
    private Boolean isDeletable;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}