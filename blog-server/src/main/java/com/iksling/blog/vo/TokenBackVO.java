package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@ApiModel(description = "令牌后台VO")
public class TokenBackVO {
    /**
     * id
     */
    @NotNull(message = "{V0031}")
    @ApiModelProperty(name = "id", value = "id", dataType = "Integer")
    private Integer id;

    /**
     * 过期时间
     */
    @ApiModelProperty(name = "expireTime", value = "过期时间", dataType = "Date")
    private Date expireTime;

    /**
     * 访问密令
     */
    @Size(min = 1, max = 100, message = "{V0065}")
    @ApiModelProperty(name = "accessToken", value = "访问密令", dataType = "String")
    private String accessToken;

    /**
     * 有效次数
     */
    @Range(min = -1, max = Integer.MAX_VALUE, message = "{V0066}")
    @ApiModelProperty(name = "effectiveCount", value = "有效次数", dataType = "Integer")
    private Integer effectiveCount;
}
