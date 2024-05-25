package com.chen.srb.core.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "IntegralGradeReturn对象",description = "积分等级表")
public class IntegralGrade implements Serializable {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "积分区间开始")
    private Integer integralStart;

    @ApiModelProperty(value = "积分区间结束")
    private Integer integralEnd;

    @ApiModelProperty(value = "借款额度")
    private BigDecimal borrowAmount;

    @ApiModelProperty(value = "创建时间", example = "2019-01-01 8:00:00")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "更新时间", example = "2019-01-01 8:00:00")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除(1:已删除，0:未删除)")
    @TableField(value = "is_deleted")
    private boolean is_deleted;
}
