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
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation("获取会员分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R list(@RequestBody UserInfoQuery userInfoQuery,
                  @PathVariable Long page,
                  @PathVariable Long limit){
        Page<UserInfo> pageParam = new Page<>(page, limit);
        IPage<UserInfo> listPage = userInfoService.listPage(pageParam,userInfoQuery);
        return R.ok().data("pageModel",listPage);

    }
}
