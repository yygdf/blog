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
public class LabelsBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 标签名
     */
    private String label;

    /**
     * 标签名2
     */
    private String label2;

    /**
     * 子标签list
     */
    private List<LabelsBackDTO> children;
}
