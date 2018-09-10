package com.boris.vo.es;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "es索引相关参数")
@Data
public class IndexVo {
    @ApiModelProperty(value = "索引名称")
    private String indexName;

    @ApiModelProperty(value = "索引类型")
    private String indexType;
}
