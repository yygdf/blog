package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName tb_day_view
 */
@TableName(value ="tb_day_view")
@Data
public class DayView implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    private Date viewDate;

    /**
     * 访问量
     */
    private Integer viewCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}