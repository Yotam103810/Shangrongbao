package com.chen.srb.sms.controller.api;


import com.chen.common.exception.Assert;
import com.chen.common.exception.BusinessException;
import com.chen.common.result.R;
import com.chen.common.result.ResponseEnum;
import com.chen.common.util.RandomUtils;
import com.chen.common.util.RegexValidateUtils;
import com.chen.srb.sms.client.CoreUserInfoClient;
import com.chen.srb.sms.service.SmsService;
import com.chen.srb.sms.util.SmsProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
@CrossOrigin //跨域
@Slf4j
public class ApiSmsController {

    @Autowired
    private SmsService smsService;


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CoreUserInfoClient coreUserInfoClient;

    /*
    接收前端的电话号码，发送短信验证码  ，判断号码不能为空，符合正则表达式
     */
    @ApiOperation("发送验证码")
    @GetMapping("/send/{mobile}")
    public R send(@PathVariable String mobile){
        //判断mobile为不为空
        if(mobile.isEmpty()){
            throw  new BusinessException(ResponseEnum.MOBILE_NULL_ERROR);
        }

        //判断手机好符不符合规范
        boolean b = RegexValidateUtils.checkCellphone(mobile);
        if(!b){
           throw new BusinessException(ResponseEnum.MOBILE_ERROR);
        }

        //是否注册
        boolean result = coreUserInfoClient.checkMobile(mobile);
        Assert.isTrue(result == false, ResponseEnum.MOBILE_EXIST_ERROR);

        //生成验证码
        String code = RandomUtils.getFourBitRandom();
        //组装短信模板参数
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);
        //发送短信
        smsService.send(mobile, SmsProperties.TEMPLATE_CODE, param);

        //存入redis
        redisTemplate.opsForValue().set("srb:sms:code:" + mobile, code, 5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");
    }
}
