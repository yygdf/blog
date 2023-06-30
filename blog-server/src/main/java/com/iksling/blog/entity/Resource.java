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
 * @TableName tb_resource
 */
@TableName(value ="tb_resource")
@Data
public class Resource implements Serializable {
    /**
     * 资源id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源路径
     */
    private String resourceUri;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 父资源id
     */
    private Integer parentId;

    /**
     * 0未禁用，1已禁用
     */
    private Integer isDisabled;

    /**
     * 0不能匿名，1可以匿名
     */
    private Integer isAnonymous;

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