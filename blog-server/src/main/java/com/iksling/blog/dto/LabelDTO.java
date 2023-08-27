package com.iksling.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelDTO {
    /**
     * 标签id
     */
    private Integer id;

    /**
     * 标签id
     */
    private Integer userId;

    /**
     * 标签名
     */
    private String label;
}
