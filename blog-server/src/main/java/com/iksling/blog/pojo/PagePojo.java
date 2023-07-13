package com.iksling.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagePojo<T> {
    /**
     * 总数
     */
    private Integer count;

    /**
     * 分页列表
     */
    private List<T> pageList;
}
