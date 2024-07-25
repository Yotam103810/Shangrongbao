package com.chen.srb.core.service;

import com.chen.srb.core.pojo.dto.BorrowInfoDTO;
import com.chen.srb.core.pojo.vo.BorrowInfoVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface BorrowerInfoService {
    BigDecimal getBorrowAmount(Long userId);

    void saveBorrowInfo(BorrowInfoVo borrowInfoVo, Long userId);

    String getBorrowerStatus(Long userId);

    List<BorrowInfoDTO> list();

    Map<String, Object> getBorrowDetailById(Long id);
}
