package com.chen.srb.core.controller.admin;


import com.chen.common.result.R;
import com.chen.srb.core.pojo.dto.BorrowInfoDTO;
import com.chen.srb.core.service.BorrowerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/core/borrowInfo")
@Api(tags = "借款管理")
public class AdminBorrowInfoController {

    @Autowired
    private BorrowerInfoService borrowerInfoService;


    @ApiOperation("借款信息列表")
    @GetMapping("/list")
    public R list(){
        List<BorrowInfoDTO> list = borrowerInfoService.list();
        return R.ok().data("list",list);
    }

    @ApiOperation("获取借款信息")
    @GetMapping("/show/{id}")
    public R show(@PathVariable("id") Long id){
        Map<String,Object> borrowInfoDetail = borrowerInfoService.getBorrowDetailById(id);
        return R.ok().data("borrowInfoDetail",borrowInfoDetail);
    }
}
