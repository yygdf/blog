package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName tb_operation_log
 */
@TableName(value ="tb_operation_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
     * 操作模块
     */
    private String optModule;

    /**
     * 操作方法
     */
    private String optMethod;

    /**
     * 操作描述
     */
    private String optDesc;

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
     * IP地址
     */
    private String ipAddress;

    /**
     * IP来源
     */
    private String ipSource;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}