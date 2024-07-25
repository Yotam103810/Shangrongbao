package com.chen.srb.core.service;

import com.chen.srb.core.pojo.vo.BorrowInfoVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface BorrowerInfoService {
    BigDecimal getBorrowAmount(Long userId);

    void saveBorrowInfo(BorrowInfoVo borrowInfoVo, Long userId);

    String getBorrowerStatus(Long userId);
}
