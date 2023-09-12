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
 * @TableName tb_multi_dir
 */
@TableName(value ="tb_multi_dir")
@Data
public class MultiDir implements Serializable {
    /**
     * 目录id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 目录链接
     */
    private String dirUrl;

    /**
     * 目录大小
     */
    private Integer dirSize;

    /**
     * 大小单位
     */
    private String dirUnit;

    /**
     * 目录路径
     */
    private String dirPath;

    /**
     * 目录描述，默认空串
     */
    private String dirDesc;

    /**
     * 目录名称
     */
    private String dirName;

    /**
     * 目录封面，默认空串
     */
    private String dirCover;

    /**
     * 0未公开，1已公开，默认1
     */
    private Boolean publicFlag;

    /**
     * 0未隐藏，1已隐藏，默认0
     */
    private Boolean hiddenFlag;

    /**
     * 0未删除，1已删除，默认0
     */
    private Boolean deletedFlag;

    /**
     * 0不可删除，1可删除，默认1
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
     * 更新人，默认null
     */
    private Integer updateUser;

    /**
     * 更新时间，默认null
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}