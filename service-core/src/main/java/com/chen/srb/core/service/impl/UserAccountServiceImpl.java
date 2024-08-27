package com.chen.srb.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chen.common.exception.Assert;
import com.chen.common.result.ResponseEnum;
import com.chen.srb.core.enums.TransTypeEnum;
import com.chen.srb.core.hfb.FormHelper;
import com.chen.srb.core.hfb.HfbConst;
import com.chen.srb.core.hfb.RequestHelper;
import com.chen.srb.core.mapper.TransFlowMapper;
import com.chen.srb.core.mapper.UserAccountMapper;
import com.chen.srb.core.mapper.UserInfoMapper;
import com.chen.srb.core.pojo.bo.TransFlowBO;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.service.TransFlowService;
import com.chen.srb.core.service.UserAccountService;
import com.chen.srb.core.util.LendNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private TransFlowService transFlowService;

    @Autowired
    private UserInfoMapper userInfoMapper;



    @Override
    public String commitCharge(BigDecimal chargeAmt, Long userId) {
        UserInfo userInfo = userInfoMapper.selectUserById(userId);
        String bindCode = userInfo.getBindCode();
        //查询绑定状态
        Assert.notEmpty(bindCode, ResponseEnum.USER_NO_BIND_ERROR);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);  //汇付宝给商家的唯一id
        paramMap.put("agentBillNo", LendNoUtils.getNo()); //账单编号日期时间加随机三位数
        paramMap.put("bindCode",bindCode); //绑定状态
        paramMap.put("chargeAmt",chargeAmt); //充值金额
        paramMap.put("feeAmt",new BigDecimal("0")); //手续费
        paramMap.put("notifyUrl",HfbConst.RECHARGE_NOTIFY_URL); //通知url
        paramMap.put("returnUrl",HfbConst.RECHARGE_RETURN_URL); //返回url通知 URL (notifyUrl)：用于服务器间的异步通知，让你的服务器知道操作结果。这种通知通常不涉及用户的直接参与，主要用于更新后台系统状态。
                                                                //返回 URL (returnUrl)：用于用户界面的同步跳转，提供用户操作完成后的即时反馈，让用户了解操作结果。
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign",sign);

        //构建充值自动提交表单
        String formStr = FormHelper.buildForm(HfbConst.RECHARGE_URL, paramMap);
        return formStr;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String notify(Map<String, Object> map) {
        log.info("充值成功：" + JSONObject.toJSONString(map));

        //判断交易流水是否存在
        String agentBillNo =(String) map.get("agentBillNo"); //商户充值订单号
        boolean isSave = transFlowService.isSaveTransFlow(agentBillNo);
        if(isSave){
            log.warn("幂等性返回");
            return "";
        }

        String bindCode =(String)map.get("bindCode");  //充值人绑定状态
        String chargeAmt = (String)map.get("chargeAmt");  //充值金额
        //更新用户账户余额
        userAccountMapper.updateAccount(bindCode,new BigDecimal(chargeAmt),new BigDecimal(0));

        //增加交易流水

        TransFlowBO transFlowBO = new TransFlowBO(agentBillNo, bindCode, new BigDecimal(chargeAmt), TransTypeEnum.RECHARGE,
                "充值");
        transFlowService.saveTransFlow(transFlowBO);
        return "success";
    }
}
