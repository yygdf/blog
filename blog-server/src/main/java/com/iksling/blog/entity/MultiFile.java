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
 * @TableName tb_multi_file
 */
@TableName(value ="tb_multi_file")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
     * 父目录id
     */
    private Integer multiDirId;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 文件描述，默认空串
     */
    private String fileDesc;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件子目录
     */
    private String fileSubDir;

    /**
     * 0未隐藏，1已隐藏，默认0
     */
    private Boolean hiddenFlag;

    /**
     * 0未删除，1已删除，默认0
     */
    private Boolean deletedFlag;

    /**
     * ip来源，默认空串
     */
    private String ipSource;

    /**
     * ip地址，默认空串
     */
    private String ipAddress;

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