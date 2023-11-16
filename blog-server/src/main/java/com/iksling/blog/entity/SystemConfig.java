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
 * @TableName tb_system_config
 */
@TableName(value ="tb_system_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfig implements Serializable {
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
     * 配置描述, 默认空串
     */
    private String configDesc;

    /**
     * 配置名
     */
    private String configName;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 0不可删除, 1可删除, 默认1
     */
    private Boolean deletableFlag;

    /**
     * 0未同步, 1已同步, 默认0
     */
    private Boolean assimilateFlag;

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