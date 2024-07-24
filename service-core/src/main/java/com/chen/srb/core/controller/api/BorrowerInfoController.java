package com.chen.srb.core.controller.api;

import com.chen.common.result.R;
import com.chen.srb.base.util.JwtUtils;
import com.chen.srb.core.service.BorrowerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
