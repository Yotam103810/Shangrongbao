package com.chen.srb.core.controller.api;

import com.alibaba.fastjson.JSON;
import com.chen.common.result.R;
import com.chen.srb.base.util.JwtUtils;
import com.chen.srb.core.hfb.RequestHelper;
import com.chen.srb.core.pojo.dto.UserBindDTO;
import com.chen.srb.core.service.UserBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/core/userBind")
@Api(tags = "会员账号绑定")
public class UserBindController {

    @Autowired
    private UserBindService userBindService;


    @ApiOperation("账户绑定提交数据")
    @PostMapping("/auth/bind")
    public R bind(@RequestBody UserBindDTO userBindDTO, HttpServletRequest httpServletRequest){
        //从httpServletRequest中获取token得到userId
        String token = httpServletRequest.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        String formStr  = userBindService.commitBindUser(userBindDTO,userId);
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation("账户绑定异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request){
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户账号绑定异步回调：" + JSON.toJSONString(paramMap));
        //校验签名
        if(!RequestHelper.isSignEquals(paramMap)) {
            log.error("用户账号绑定异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
        //修改绑定状态
        userBindService.notify(paramMap);
        return "success";
    }
}
