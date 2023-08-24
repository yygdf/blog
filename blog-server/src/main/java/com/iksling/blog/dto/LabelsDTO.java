package com.iksling.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelsDTO {
    /**
     * 标签id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 标签名
     */
    private String label;

    /**
     * 子标签集合
     */
    private List<LabelsDTO> children;
}
