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
     * 父资源id
     */
    private Integer parentId;

    /**
     * 资源路径
     */
    private String resourceUri;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 0未禁用，1已禁用
     */
    private Boolean isDisabled;

    /**
     * 0未匿名，1已匿名
     */
    private Boolean isAnonymous;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}