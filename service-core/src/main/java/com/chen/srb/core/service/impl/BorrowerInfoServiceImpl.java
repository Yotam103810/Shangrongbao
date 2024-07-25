package com.chen.srb.core.service.impl;

import com.alibaba.excel.util.CollectionUtils;
import com.chen.common.exception.Assert;
import com.chen.common.result.ResponseEnum;
import com.chen.srb.core.enums.BorrowInfoStatusEnum;
import com.chen.srb.core.enums.BorrowerStatusEnum;
import com.chen.srb.core.enums.UserBindEnum;
import com.chen.srb.core.mapper.*;
import com.chen.srb.core.pojo.dto.BorrowInfoDTO;
import com.chen.srb.core.pojo.entity.Borrower;
import com.chen.srb.core.pojo.entity.BorrowerInfo;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.pojo.vo.BorrowInfoVo;
import com.chen.srb.core.pojo.vo.BorrowerDetailVO;
import com.chen.srb.core.service.BorrowerInfoService;
import com.chen.srb.core.service.BorrowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BorrowerInfoServiceImpl implements BorrowerInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IntegerGradeMapper integerGradeMapper;

    @Autowired
    private BorrowInfoMapper borrowInfoMapper;

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private BorrowerMapper borrowerMapper;

    @Autowired
    private BorrowerService borrowerService;

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
        Assert.isTrue(userInfo.getBindStatus().intValue() == UserBindEnum.BIND_OK.getStatus().intValue(), ResponseEnum.USER_NO_BIND_ERROR);
        //判断用户信息认证状态
        Assert.isTrue(userInfo.getBorrowAuthStatus().intValue() == BorrowerStatusEnum.AUTH_OK.getStatus().intValue(), ResponseEnum.USER_NO_AMOUNT_ERROR);

        //判断借款额度是否超限，用用户的借款金额与积分登记表中的借款额度做对比
        BigDecimal borrowAmount = integerGradeMapper.getBorrowAmount(userInfo.getIntegral());
        Assert.isTrue(borrowInfoVo.getAmount().doubleValue() <= borrowAmount.doubleValue(), ResponseEnum.USER_AMOUNT_LESS_ERROR);

        //存储数据
        BorrowerInfo borrowerInfo = new BorrowerInfo();
        borrowInfoVo.setUserId(userId);
        borrowInfoVo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus());
        borrowInfoVo.setBorrowYearRate(borrowInfoVo.getBorrowYearRate().divide(new BigDecimal(100)));
        BeanUtils.copyProperties(borrowInfoVo, borrowerInfo);

        borrowInfoMapper.insertBorrowInfo(borrowerInfo);

    }

    @Override
    public String getBorrowerStatus(Long userId) {
        List<Object> list = borrowInfoMapper.getBorrowerStatus(userId);
        if (list.size() == 0) {
            return BorrowerStatusEnum.NO_AUTH.getMsg();
        }
        return BorrowInfoStatusEnum.getMsgByStatus((Integer) list.get(0));

    }

    @Override
    public List<BorrowInfoDTO> list() {
        List<BorrowerInfo> borrowerInfoList = borrowInfoMapper.list();
        if (CollectionUtils.isEmpty(borrowerInfoList)) {
            return Collections.emptyList();
        }
        return borrowerInfoList.stream()
                .map(borrowerInfo -> {
                    BorrowInfoDTO borrowInfoDTO = new BorrowInfoDTO();
                    BeanUtils.copyProperties(borrowerInfo, borrowInfoDTO);
                    //获取还款方式
                    String returnMethod = dictMapper.getReturnMethod(borrowerInfo.getReturnMethod());
                    //获取资金用途
                    String moneyUse = dictMapper.getMoneyUse(borrowerInfo.getMoneyUse());
                    //获取审核状态
                    String status = BorrowInfoStatusEnum.getMsgByStatus(borrowerInfo.getStatus());
                    borrowInfoDTO.getParam().put("returnMethod",returnMethod);
                    borrowInfoDTO.getParam().put("moneyUse",moneyUse);
                    borrowInfoDTO.getParam().put("status",status);
                    return borrowInfoDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getBorrowDetailById(Long id) {
        //根据id查询借款信息
        BorrowerInfo borrowerInfo = borrowInfoMapper.selectBorrowInfoById(id);
        String returnMethod = dictMapper.getReturnMethod(borrowerInfo.getReturnMethod());
        String moneyUse = dictMapper.getMoneyUse(borrowerInfo.getMoneyUse());
        String msgByStatus = BorrowInfoStatusEnum.getMsgByStatus(borrowerInfo.getStatus());
        borrowerInfo.getParam().put("returnMethod",returnMethod);
        borrowerInfo.getParam().put("moneyUse",moneyUse);
        borrowerInfo.getParam().put("msgByStatus",msgByStatus);

        //根据userid查询借款人信息
        Borrower borrower = borrowerMapper.getBorrowerDetail(borrowerInfo.getUserId());
        BorrowerDetailVO borrowerDetailVOById = borrowerService.getBorrowerDetailVOById(borrower.getId());

        Map<String, Object> result  = new HashMap<>();
        result.put("borrowerInfo",borrowerInfo);
        result.put("borrower",borrowerDetailVOById);

        return result;
    }
}
