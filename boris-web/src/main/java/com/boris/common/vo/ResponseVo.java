package com.boris.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "通用返回结果")
@Data
public class ResponseVo<T> {
    @ApiModelProperty(value = "是否成功")
    private boolean success = true;

    @ApiModelProperty(value = "状态码")
    private int code = 200;

    @ApiModelProperty(value = "描述")
    private String msg;

    private T data;
}
