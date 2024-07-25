package com.chen.srb.core.service.impl;

import com.chen.common.exception.Assert;
import com.chen.common.result.ResponseEnum;
import com.chen.srb.core.enums.BorrowInfoStatusEnum;
import com.chen.srb.core.enums.BorrowerStatusEnum;
import com.chen.srb.core.enums.UserBindEnum;
import com.chen.srb.core.mapper.BorrowInfoMapper;
import com.chen.srb.core.mapper.IntegerGradeMapper;
import com.chen.srb.core.mapper.UserInfoMapper;
import com.chen.srb.core.pojo.entity.BorrowerInfo;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.pojo.vo.BorrowInfoVo;
import com.chen.srb.core.service.BorrowerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class BorrowerInfoServiceImpl implements BorrowerInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IntegerGradeMapper integerGradeMapper;

    @Autowired
    private BorrowInfoMapper borrowInfoMapper;

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

    /**
     * 查询用户信息表找到绑定状态和借款人认证状态。接着对比借款额度是否超限。然后存储数据
     * @param borrowInfoVo
     * @param userId
     */
    @Override
    public void saveBorrowInfo(BorrowInfoVo borrowInfoVo, Long userId) {
        UserInfo userInfo = userInfoMapper.selectUserById(userId);
        //判断用户绑定状态
        Assert.isTrue(userInfo.getBindStatus().intValue() == UserBindEnum.BIND_OK.getStatus().intValue(),ResponseEnum.USER_NO_BIND_ERROR);
        //判断用户信息认证状态
        Assert.isTrue(userInfo.getBorrowAuthStatus().intValue() == BorrowerStatusEnum.AUTH_OK.getStatus().intValue(),ResponseEnum.USER_NO_AMOUNT_ERROR);

        //判断借款额度是否超限，用用户的借款金额与积分登记表中的借款额度做对比
        BigDecimal borrowAmount = integerGradeMapper.getBorrowAmount(userInfo.getIntegral());
        Assert.isTrue(borrowInfoVo.getAmount().doubleValue() <= borrowAmount.doubleValue(),ResponseEnum.USER_AMOUNT_LESS_ERROR);

        //存储数据
        BorrowerInfo borrowerInfo = new BorrowerInfo();
        borrowInfoVo.setUserId(userId);
        borrowInfoVo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus());
        borrowInfoVo.setBorrowYearRate(borrowInfoVo.getBorrowYearRate().divide(new BigDecimal(100)));
        BeanUtils.copyProperties(borrowInfoVo,borrowerInfo);

        borrowInfoMapper.insertBorrowInfo(borrowerInfo);

    }

    @Override
    public String getBorrowerStatus(Long userId) {
        List<Object> list = borrowInfoMapper.getBorrowerStatus(userId);
        if(list.size() == 0){
            return BorrowerStatusEnum.NO_AUTH.getMsg();
        }
        String statusMsg = BorrowInfoStatusEnum.getMsgByStatus((Integer) list.get(0));
        return statusMsg;
    }
}
