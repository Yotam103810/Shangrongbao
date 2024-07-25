package com.chen.srb.core.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class BorrowInfoDTO {

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "借款金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "借款期限")
    private Integer period;

    @ApiModelProperty(value = "年化利率")
    private BigDecimal borrowYearRate;

    @ApiModelProperty(value = "还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本")
    private Integer returnMethod;

    @ApiModelProperty(value = "资金用途")
    private Integer moneyUse;

    @ApiModelProperty(value = "状态（0：默认 1：审核通过 -1：审核不通过）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;




    //扩展字段
    @ApiModelProperty(value = "姓名")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty(value = "手机")
    @TableField(exist = false)
    private String mobile;

    @ApiModelProperty(value = "其他参数")
    @TableField(exist = false)
    private Map<String,Object> param = new HashMap<>();
}
