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
 * @TableName tb_menu
 */
@TableName(value ="tb_menu")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {
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
     * 菜单图标
     */
    private String icon;

    /**
     * 排序指标, 默认100
     */
    private Integer rank;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单名称(en)
     */
    private String nameEn;

    /**
     * 菜单组件, 默认Layout
     */
    private String component;

    /**
     * 0未隐藏, 1已隐藏, 默认0
     */
    private Boolean hideFlag;

    /**
     * 0未隐藏, 1已隐藏, 默认0
     */
    private Boolean hiddenFlag;

    /**
     * 0未禁用, 1已禁用, 默认0
     */
    private Boolean disabledFlag;

    /**
     * 0不可删除, 1可删除, 默认1
     */
    private Boolean deletableFlag;

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