package com.chen.srb.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.common.result.R;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.pojo.query.UserInfoQuery;
import com.chen.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(tags = "会员管理")
@RequestMapping("/admin/core/userInfo")
@CrossOrigin
public class AdminUserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 根据查询条件获取UserInfo分页列表
     * @param userInfoQuery
     * @param pageSize
     * @param pageNum
     * @return
     */
    @ApiOperation("获取会员分页列表")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public R list(@RequestBody UserInfoQuery userInfoQuery,
                  @PathVariable("pageNum") Integer pageNum,
                  @PathVariable("pageSize") Integer pageSize){
        UserInfo userInfo = userInfoService.listPage(pageNum,pageSize, userInfoQuery);
        return R.ok().data("pageModel",userInfo);
    }

    /**
     * 会员锁定和解锁
     * @param id
     * @param status
     * @return
     */
    @ApiOperation("锁定和解锁")
    @PutMapping("/lock/{id}/{status}")
    public R lock(@PathVariable("id") Long id,
                  @PathVariable("status") Integer status){
        userInfoService.lock(id,status);
        return R.ok().message(status == 1 ? "解锁成功":"锁定成功");
    }
}
