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
 * @TableName tb_multi_file
 */
@TableName(value ="tb_multi_file")
@Data
public class MultiFile implements Serializable {
    /**
     * 文件id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 文件描述
     */
    private String fileDesc;

    /**
     * 父目录id
     */
    private Integer multiDirId;

    /**
     * 子目录
     */
    private String subDir;

    /**
     * 0未隐藏，1已隐藏
     */
    private Boolean isHidden;

    /**
     * 0未删除，1已删除
     */
    private Boolean isDeleted;

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