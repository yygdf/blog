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
     * 文件描述, 默认空串
     */
    private String fileDesc;

    /**
     * 文件标识, 默认0
     */
    private Integer fileMark;

    /**
     * 文件名称
     */
    private Long fileName;

    /**
     * 文件大小, 默认-1
     */
    private Long fileSize;

    /**
     * 文件封面, 默认空串
     */
    private String fileCover;

    /**
     * 文件完整路径
     */
    private String fileFullPath;

    /**
     * 文件拓展名, 默认空串
     */
    private String fileExtension;

    /**
     * 文件新名称, 默认-1
     */
    private Long fileNameNew;

    /**
     * 文件原始名称
     */
    private String fileNameOrigin;

    /**
     * 0未公开, 1已公开, 默认1
     */
    private Boolean publicFlag;

    /**
     * 0未隐藏, 1已隐藏, 默认0
     */
    private Boolean hiddenFlag;

    /**
     * 0不可删除, 1可删除, 默认1
     */
    private Boolean deletableFlag;

    /**
     * 删除次数, 默认0
     */
    private Integer deletedCount;

    /**
     * ip来源, 默认空串
     */
    private String ipSource;

    /**
     * ip地址, 默认空串
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