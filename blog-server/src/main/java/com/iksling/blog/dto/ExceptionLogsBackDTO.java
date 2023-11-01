package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ExceptionLogsBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 操作路径
     */
    private String optUri;

    /**
     * 操作类型, 1上传, 2删除, 3修改, 4查询, 5新增或修改
     */
    private Integer optType;

    /**
     * 操作描述, 默认空串
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
     * 异常简略信息
     */
    private String exceptionMessage;

    /**
     * 异常堆栈信息
     */
    private String exceptionStackTrace;

    /**
     * ip来源, 默认空串
     */
    private String ipSource;

    /**
     * ip地址, 默认空串
     */
    private String ipAddress;

    /**
     * 创建时间
     */
    private Date createTime;
}