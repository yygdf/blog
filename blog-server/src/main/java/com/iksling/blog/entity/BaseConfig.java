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
 * @TableName tb_base_config
 */
@TableName(value ="tb_base_config")
@Data
public class BaseConfig implements Serializable {
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
     * 配置描述
     */
    private String configDesc;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 配置名
     */
    private String configName;

    /**
     * 0未删除，1已删除
     */
    private Boolean isDeleted;

    /**
     * 0不可删除，1可删除
     */
    private Boolean isDeletable;

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