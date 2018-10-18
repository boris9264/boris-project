package com.boris.vo.es;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "商品信息")
public class ProductVo extends IndexVo{
    @ApiModelProperty(value = "唯一标志")
    private String id;
    @ApiModelProperty(value = "商品名称")
    private String productName;
    @ApiModelProperty(value = "商品类型")
    private String productType;
    @ApiModelProperty(value = "商品编码")
    private String sku;
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    @ApiModelProperty(value = "库存")
    private int Stock;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
