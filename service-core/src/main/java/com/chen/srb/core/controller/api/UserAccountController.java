package com.chen.srb.core.controller.api;

import com.alibaba.fastjson.JSON;
import com.chen.common.result.R;
import com.chen.srb.base.util.JwtUtils;
import com.chen.srb.core.hfb.RequestHelper;
import com.chen.srb.core.service.UserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/core/userAccount")
@Api(tags = "会员账户")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @ApiOperation("充值")
    @PostMapping("/auth/commitCharge/{chargeAmt}")
    public R commitCharge(@PathVariable("chargeAmt")BigDecimal chargeAmt, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        String formStr = userAccountService.commitCharge(chargeAmt, userId);
        return R.ok().data("formStr",formStr);
    }

    @ApiOperation("用户充值异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request){
        Map<String, Object> map = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户充值异步回调：" + JSON.toJSONString(map));

        //校验签名
        if(RequestHelper.isSignEquals(map)){
            //充值成功
            if("0001".equals(map.get("resultCode"))){
                return userAccountService.notify(map);   //修改账户余额
            }else {
                log.info("用户充值异步回调充值失败：" + JSON.toJSONString(map));
                return "success";
            }
        }else {
            log.info("用户充值异步回调签名错误：" + JSON.toJSONString(map));
            return "fail";
        }
    }
}
