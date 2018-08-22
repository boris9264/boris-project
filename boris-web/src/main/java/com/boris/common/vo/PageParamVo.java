package com.boris.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "分页参数")
@Data
public class PageParamVo {
    @ApiModelProperty(notes = "页码")
    private Integer pageNum;

    @ApiModelProperty(notes = "每页条数")
    private Integer pageSize;
}
