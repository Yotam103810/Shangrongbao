package com.chen.srb.core.controller.admin;

import com.chen.common.result.R;
import com.chen.srb.core.pojo.entity.Lend;
import com.chen.srb.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/core/lend")
@Api(tags = "标的管理")
public class AdminLendController {

    @Autowired
    private LendService lendService;


    /**
     * 查询所有标
     * @return
     */
    @ApiOperation("标的列表")
    @GetMapping("/list")
    public R list(){
        List<Lend> list = lendService.selectList();
        return R.ok().data("LendList",list);
    }
}
