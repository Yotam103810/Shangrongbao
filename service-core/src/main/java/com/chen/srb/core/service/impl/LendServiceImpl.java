package com.chen.srb.core.service.impl;

import com.alibaba.excel.util.CollectionUtils;
import com.chen.srb.core.enums.LendStatusEnum;
import com.chen.srb.core.mapper.BorrowerMapper;
import com.chen.srb.core.mapper.DictMapper;
import com.chen.srb.core.mapper.LendMapper;
import com.chen.srb.core.pojo.entity.Borrower;
import com.chen.srb.core.pojo.entity.BorrowerInfo;
import com.chen.srb.core.pojo.entity.Lend;
import com.chen.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.chen.srb.core.pojo.vo.BorrowerDetailVO;
import com.chen.srb.core.service.BorrowerService;
import com.chen.srb.core.service.LendService;
import com.chen.srb.core.util.LendNoUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LendServiceImpl implements LendService {

    @Autowired
    private LendMapper lendMapper;

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private BorrowerMapper borrowerMapper;

    @Autowired
    private BorrowerService borrowerService;


    @Override
    public void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowerInfo borrowerInfo) {
        Lend lend = new Lend();
        lend.setUserId(borrowerInfo.getUserId());
        lend.setBorrowInfoId(borrowerInfo.getId());
        lend.setLendNo(LendNoUtils.getLendNo());
        lend.setTitle(borrowInfoApprovalVO.getTitle());
        lend.setAmount(borrowerInfo.getAmount());
        lend.setPeriod(borrowerInfo.getPeriod());
        lend.setLendYearRate(borrowInfoApprovalVO.getLendYearRate().divide(new BigDecimal(100)));
        lend.setServiceRate(borrowInfoApprovalVO.getServiceRate().divide(new BigDecimal(100)));
        lend.setReturnMethod(borrowerInfo.getReturnMethod());
        lend.setLowestAmount(new BigDecimal(100));
        lend.setInvestAmount(new BigDecimal(0));
        lend.setInvestNum(0);
        lend.setPublishDate(LocalDateTime.now());

        //起息日期
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate lendStartDate = LocalDate.parse(borrowInfoApprovalVO.getLendStartDate(), dtf);
        lend.setLendStartDate(lendStartDate);

        //结束日期
        LocalDate localDate = lendStartDate.plusMonths(borrowerInfo.getPeriod());
        lend.setLendEndDate(localDate);

        //描述
        lend.setLendInfo(borrowInfoApprovalVO.getLendInfo());

        //平台预期收益
        //月年华收益
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);

        //平台预期收益 = 标的金额 * 月年化 * 借款期数
        BigDecimal expectAmount  = lend.getAmount().multiply(monthRate).multiply(new BigDecimal(lend.getPeriod()));
        lend.setExpectAmount(expectAmount);

        //实际收益
        lend.setRealAmount(new BigDecimal(0));
        //状态
        lend.setStatus(LendStatusEnum.INVEST_RUN.getStatus());
        //审核时间
        lend.setCheckTime(LocalDateTime.now());
        //审核人
        lend.setCheckAdminId(1L);
        lendMapper.insertLend(lend);

    }

    @Override
    public List<Lend> selectList() {
        List<Lend> lendList = lendMapper.selectList();
//        lendList.parallelStream().forEach(
//                lend -> {
//                    String returnMethod = dictMapper.getReturnMethod(lend.getReturnMethod());
//                    String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
//                    lend.getParam().put("returnMethod",returnMethod);
//                    lend.getParam().put("status",status);
//                }
//        );// 嗷嗷快 多线程
        // 判断空指针
        if (CollectionUtils.isEmpty(lendList)){
            return new ArrayList<>();
        }
        lendList.forEach(lend -> {
            String returnMethod = dictMapper.getReturnMethod(lend.getReturnMethod());
            String status = LendStatusEnum.getMsgByStatus(lend.getStatus());

                lend.getParam().put("returnMethod",returnMethod == null? "":returnMethod);
            lend.getParam().put("status",status);
        });
        return lendList;
    }

    @Override
    public Map<String, Object> getLendDetail(Long id) {
        if(ObjectUtils.isEmpty(id) || id < 0){
            return new HashMap<>();
        }
        //获取标的信息
        Lend lend = lendMapper.getLendById(id);
        if(ObjectUtils.isEmpty(lend)){
            return new HashMap<>();
        }
        //组装数据
        String returnMethod = dictMapper.getReturnMethod(lend.getReturnMethod());
        String msgByStatus = LendStatusEnum.getMsgByStatus(lend.getStatus());
        lend.getParam().put("returnMethod",returnMethod);
        lend.getParam().put("msgByStatus",msgByStatus);

        //根据user_id获取借款人对象
        Borrower borrower = borrowerMapper.getBorrowerDetailByUserId(lend.getUserId());
        if(ObjectUtils.isEmpty(borrower)){
            throw new RuntimeException("borrower对象为空");
        }
        BorrowerDetailVO borrowerDetailVOById = borrowerService.getBorrowerDetailVOById(borrower.getId());

        //封装map
        Map<String,Object> map = new HashMap<>();
        map.put("lend",lend);
        map.put("borrower",borrowerDetailVOById);

        return map;
    }
}
