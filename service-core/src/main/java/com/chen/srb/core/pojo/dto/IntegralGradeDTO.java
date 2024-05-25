package com.chen.srb.core.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "请求的积分等级对象")
public class IntegralGradeDTO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "积分区间开始")
    private Integer integralStart;

    @ApiModelProperty(value = "积分区间结束")
    private Integer integralEnd;

    @ApiModelProperty(value = "借款额度")
    private BigDecimal borrowAmount;


}
