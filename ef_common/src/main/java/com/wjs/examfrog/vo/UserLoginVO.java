package com.wjs.examfrog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVO implements Serializable {

    /**
     * swagger: 置顶
     */
    @ApiModelProperty(value = "token", position = 1)
    private String token;
    /**
     * 联表查询
     */
    @ApiModelProperty(value = "用户详细信息", position = 2)
    private UserDetailsVO userInfo;

}
