package com.chen.srb.core.controller.api;

import com.chen.common.result.R;
import com.chen.srb.base.util.JwtUtils;
import com.chen.srb.core.pojo.vo.BorrowInfoVo;
import com.chen.srb.core.service.BorrowerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Api(tags = "借款信息")
@RestController
@RequestMapping("/api/core/borrowInfo")
@Slf4j
public class BorrowerInfoController {
    @Autowired
    private BorrowerInfoService borrowerInfoService;

    @ApiOperation("获取借款额度")
    @GetMapping("/auth/getBorrowAmount/{userId}")
    public R getBorrowAmount(@PathVariable("userId") Long userId){
//        String token = httpServletRequest.getHeader("token");
//        Long userId = JwtUtils.getUserId(token);
        BigDecimal borrowAmount = borrowerInfoService.getBorrowAmount(userId);
        return R.ok().data("borrowAmount",borrowAmount);
    }

    @ApiOperation("提交借款申请")
    @PostMapping("/auth/save")
    public R save(@RequestBody BorrowInfoVo borrowInfoVo){
//        //获取用户ID
//        String token = httpServletRequest.getHeader("token");
//        Long userId = JwtUtils.getUserId(token);
        Long userId = Long.valueOf(2);
        borrowerInfoService.saveBorrowInfo(borrowInfoVo,userId);
        return R.ok().message("提交成功");
    }

    @ApiOperation("获取借款申请审批状态")
    @GetMapping("/auth/getBorrowInfoStatus")
    public R getBorrowerStatus(){
//        String token = request.getHeader("token");
//        Long userId = JwtUtils.getUserId(token);
        Long userId = Long.valueOf(2);
        String status = borrowerInfoService.getBorrowerStatus(userId);
        return R.ok().data("borrowerStatus",status);
    }
}
