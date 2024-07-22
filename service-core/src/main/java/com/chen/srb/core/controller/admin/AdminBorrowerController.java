package com.chen.srb.core.controller.admin;

import com.chen.common.result.R;
import com.chen.srb.core.pojo.entity.Borrower;
import com.chen.srb.core.pojo.vo.BorrowerVO;
import com.chen.srb.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/core/borrower")
@Api(tags = "借款人管理")
@Slf4j
public class AdminBorrowerController {

    @Autowired
    private BorrowerService borrowerService;

    @ApiOperation("获取借款人分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R listPage(@Param("page") Integer page,
                      @Param("limit") Integer limit,
                      @RequestParam String keyword){
        BorrowerVO list = borrowerService.listPage(page,limit,keyword);
        return R.ok().data("list",list);
    }
}
