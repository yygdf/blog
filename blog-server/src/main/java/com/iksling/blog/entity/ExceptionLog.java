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
 * @TableName tb_exception_log
 */
@TableName(value ="tb_exception_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionLog implements Serializable {
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
     * 操作路径
     */
    private String optUri;

    /**
     * 操作类型, 1新增, 2删除, 3修改, 4查询, 5新增或修改
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
     * 0不是非法, 1是非法, 默认0
     */
    private Boolean illegalFlag;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}