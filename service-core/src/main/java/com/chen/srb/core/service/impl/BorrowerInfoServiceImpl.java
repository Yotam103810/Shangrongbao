package com.chen.srb.core.service.impl;

import com.chen.common.exception.Assert;
import com.chen.common.result.ResponseEnum;
import com.chen.srb.core.mapper.IntegerGradeMapper;
import com.chen.srb.core.mapper.UserInfoMapper;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.service.BorrowerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class BorrowerInfoServiceImpl implements BorrowerInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IntegerGradeMapper integerGradeMapper;

    /**
     * 根据userid查出用户积分，再去积分等级表中确认借款额度
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public BigDecimal getBorrowAmount(Long userId) {
        //获取用户积分
        UserInfo userInfo = userInfoMapper.selectUserById(userId);
        Assert.notNull(userInfo, ResponseEnum.LOGIN_MOBILE_ERROR);
        int userIntegral = userInfo.getIntegral();

        //查询借款额度
        BigDecimal borrowAmount = integerGradeMapper.getBorrowAmount(userIntegral);
        if(borrowAmount == null){
            borrowAmount = new BigDecimal(0);
        }
        return borrowAmount;
    }
}
