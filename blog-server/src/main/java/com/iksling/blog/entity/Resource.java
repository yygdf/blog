package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName tb_resource
 */
@TableName(value ="tb_resource")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 父id, 默认-1
     */
    private Integer parentId;

    /**
     * 资源路径, 默认空串
     */
    private String resourceUri;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 请求方式, 默认空串
     */
    private String resourceRequestMethod;

    /**
     * 0未禁用, 1已禁用, 默认0
     */
    private Boolean disabledFlag;

    /**
     * 0不可删除, 1可删除, 默认1
     */
    private Boolean deletableFlag;

    /**
     * 0未匿名, 1已匿名, 默认0
     */
    private Boolean anonymousFlag;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人, 默认null
     */
    private Integer updateUser;

    /**
     * 更新时间, 默认null
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}