package com.chen.srb.core.service.impl;

import com.chen.common.exception.Assert;
import com.chen.common.result.ResponseEnum;
import com.chen.srb.core.enums.UserBindEnum;
import com.chen.srb.core.hfb.FormHelper;
import com.chen.srb.core.hfb.HfbConst;
import com.chen.srb.core.hfb.RequestHelper;
import com.chen.srb.core.mapper.UserBindMapper;
import com.chen.srb.core.mapper.UserInfoMapper;
import com.chen.srb.core.pojo.dto.UserBindDTO;
import com.chen.srb.core.pojo.entity.UserBind;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.service.UserBindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
public class UserBindServiceImpl implements UserBindService {

    @Autowired
    private UserBindMapper userBindMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public String commitBindUser(UserBindDTO userBindDTO, Long userId) {
        //查询身份证号码是否绑定
        UserBind userBind = userBindMapper.selectCount(userBindDTO.getIdCard());
        Assert.isNull(userBind, ResponseEnum.USER_BIND_IDCARD_EXIST_ERROR);

        //查询用户绑定信息
        userBind = userBindMapper.selectUserBind(userId);
        //判断是否有绑定记录
        if(userBind == null){
            //如果未创建绑定记录，则创建一条记录
            userBind = new UserBind();
            userBind.setUserId(userId);
            userBind.setName(userBindDTO.getName());
            userBind.setIdCard(userBindDTO.getIdCard());
            userBind.setBankNo(userBindDTO.getBankNo());
            userBind.setBankType(userBindDTO.getBankType());
            userBind.setMobile(userBindDTO.getMobile());
            userBind.setStatus(UserBindEnum.NO_BIND.getStatus());
            userBindMapper.insertUserBind(userBind);
        }else {
            //曾经跳转到托管平台，但是未操作完成，此时将用户最新填写的数据同步到userBind对象(填写的绑定信息不全)
            userBind.setName(userBindDTO.getName());
            userBind.setIdCard(userBindDTO.getIdCard());
            userBind.setBankNo(userBindDTO.getBankNo());
            userBind.setBankType(userBindDTO.getBankType());
            userBind.setMobile(userBindDTO.getMobile());
            userBindMapper.updateUserBind(userBind);
        }
        //构建充值自动提交表单
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentUserId",userId);
        paramMap.put("idCard",userBindDTO.getIdCard());
        paramMap.put("personalName", userBindDTO.getName());
        paramMap.put("bankType", userBindDTO.getBankType());
        paramMap.put("bankNo", userBindDTO.getBankNo());
        paramMap.put("mobile", userBindDTO.getMobile());
        paramMap.put("returnUrl", HfbConst.USERBIND_RETURN_URL);
        paramMap.put("notifyUrl", HfbConst.USERBIND_NOTIFY_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        paramMap.put("sign", RequestHelper.getSign(paramMap));

        String formStr = FormHelper.buildForm(HfbConst.USERBIND_URL, paramMap);
        return formStr;
    }

    @Override
    public void notify(Map<String, Object> paramMap) {
        String bindCode = (String) paramMap.get("bindCode");
        //会员id
        String agentUserId = (String) paramMap.get("agentUserId");

        //根据user_id查询user_bind记录,更新用户绑定表
        UserBind userBind = userBindMapper.selectUserBind(Long.valueOf(agentUserId));
        userBind.setBindCode(bindCode);
        userBind.setStatus(UserBindEnum.BIND_OK.getStatus());
        userBindMapper.updateUserBind(userBind);

        //更改用户表
        UserInfo userInfo = userInfoMapper.selectUserById(Long.valueOf(agentUserId));
        userInfo.setBindCode(bindCode);
        userInfo.setName(userBind.getName());
        userInfo.setIdCard(userBind.getIdCard());
        userInfo.setBindStatus(UserBindEnum.BIND_OK.getStatus());
        userInfo.setUpdateTime(now());
        userInfoMapper.updateUserInfo(userInfo);
    }
}
