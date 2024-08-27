package com.chen.srb.core.service.impl;

import com.chen.srb.core.mapper.TransFlowMapper;
import com.chen.srb.core.mapper.UserInfoMapper;
import com.chen.srb.core.pojo.bo.TransFlowBO;
import com.chen.srb.core.pojo.entity.TransFlow;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.service.TransFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransFlowServiceImpl implements TransFlowService {

    @Autowired
    private TransFlowMapper transFlowMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public void saveTransFlow(TransFlowBO transFlowBO) {
        //获取用户基本信息 user_info
        String bindCode = transFlowBO.getBindCode();
        UserInfo userInfo = userInfoMapper.selectUserInfoByBindCode(bindCode);

        //存储交易流水数据
        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userInfo.getId());
        transFlow.setUserName(userInfo.getName());
        transFlow.setTransNo(transFlowBO.getAgentBillNo());
        transFlow.setTransType(transFlowBO.getTransTypeEnum().getTransType());
        transFlow.setTransTypeName(transFlowBO.getTransTypeEnum().getTransTypeName());
        transFlow.setTransAmount(transFlowBO.getAmount());
        transFlow.setMemo(transFlowBO.getMemo());
        transFlowMapper.insert(transFlow);
    }

    @Override
    public boolean isSaveTransFlow(String agentBillNo) {
        int count = transFlowMapper.selectTransFlow(agentBillNo);
        if(count > 0){
            return true;
        }
        return false;
    }
}
