package com.chen.srb.core.controller.api;

import com.chen.common.exception.Assert;
import com.chen.common.result.R;
import com.chen.srb.base.util.JwtUtils;
import com.chen.srb.core.pojo.dto.BorrowerDTO;
import com.chen.srb.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/core/borrower")
@Slf4j
@Api(tags = "借款人")
public class BorrowerController {
    @Autowired
    private BorrowerService borrowerService;

    /**
     * 保存借款人的信息，通过token获取借款人的用户id，来查询身份手机姓名等信息
     * @param borrowerDTO
     * @param request
     * @return
     */
    @ApiOperation("保存借款人信息")
    @PostMapping("/auth/save")
    public R save(@RequestBody BorrowerDTO borrowerDTO, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        borrowerService.saveBorrower(borrowerDTO,userId);
        return R.ok().message("保存成功");
    }


}
