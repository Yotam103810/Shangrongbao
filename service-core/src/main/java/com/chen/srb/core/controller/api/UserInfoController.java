package com.chen.srb.core.controller.api;

import com.chen.common.exception.Assert;
import com.chen.common.result.R;
import com.chen.common.result.ResponseEnum;
import com.chen.common.util.RegexValidateUtils;
import com.chen.srb.base.util.JwtUtils;
import com.chen.srb.core.pojo.dto.LoginDTO;
import com.chen.srb.core.pojo.dto.RegisterDTO;
import com.chen.srb.core.pojo.vo.UserInfoVO;
import com.chen.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "会员接口")
@RestController
@RequestMapping("/api/core/userInfo")
@Slf4j
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 会员注册
     * @param registerDTO
     * @return
     */
    @GetMapping("/register")
    public R register(@RequestBody RegisterDTO registerDTO){
        //判断手机号，验证码，密码
        //手机号不能为空
        Assert.notEmpty(registerDTO.getMobile(), ResponseEnum.MOBILE_NULL_ERROR);
        //手机号符合正则规范
        Assert.isTrue(RegexValidateUtils.checkCellphone(registerDTO.getMobile()),ResponseEnum.MOBILE_ERROR);
        //验证码不能为空
        Assert.notEmpty(registerDTO.getCode(),ResponseEnum.CODE_NULL_ERROR);
        //密码不能为空
        Assert.notEmpty(registerDTO.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);

        //校验验证码
        String redisCode = (String) redisTemplate.opsForValue().get("srb:sms:code:" + registerDTO.getMobile());
        Assert.equals(redisCode,registerDTO.getCode(),ResponseEnum.CODE_ERROR);
        //注册
        userInfoService.register(registerDTO);
        return R.ok().message("注册成功");
    }


    @ApiOperation("会员登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginDTO loginDTO, HttpServletRequest request){
        //校验手机号
        String mobile = loginDTO.getMobile();
        String password = loginDTO.getPassword();
        Assert.notEmpty(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);

        //获取登录地址：ip同步到用户登录登记表
        String ip = request.getRemoteAddr();
        UserInfoVO userInfoVO = userInfoService.login(loginDTO,ip);

        return R.ok().data("userInfoVO",userInfoVO);
    }


    @GetMapping("/checkToken")
    public R checkToken(HttpServletRequest request){

        //获取请求头中的token
        String token = request.getHeader("token");
        boolean b = JwtUtils.checkToken(token);
        if(b){
            return R.ok();
        }else {
            return R.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }
}
