package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * @TableName tb_operation_log
 */
@TableName(value ="tb_operation_log")
@Data
@Builder
public class OperationLog implements Serializable {
    /**
     * 操作日志id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 操作路径
     */
    private String optUri;

    /**
     * 操作类型
     */
    private String optType;

    /**
     * 操作描述
     */
    private String optDesc;

    /**
     * 操作模块
     */
    private String optModule;

    /**
     * 操作方法
     */
    private String optMethod;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 响应数据
     */
    private String responseData;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * ip地址
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}