package com.iksling.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticBackDTO {
    /**
     * name
     */
    private String name;

    /**
     * value
     */
    private Integer value;
}
