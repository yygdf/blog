package com.iksling.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
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

    public PagePojo() {
        this.count = 0;
        this.pageList = new ArrayList<>();
    }
}
