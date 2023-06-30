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
 * @TableName tb_menu
 */
@TableName(value ="tb_menu")
@Data
public class Menu implements Serializable {
    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单组件
     */
    private String component;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 父菜单id
     */
    private Integer parentId;

    /**
     * 排序指标
     */
    private Integer orderNum;

    /**
     * 0未隐藏，1已隐藏
     */
    private Integer isHidden;

    /**
     * 0未禁用，1已禁用
     */
    private Integer isDisabled;

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