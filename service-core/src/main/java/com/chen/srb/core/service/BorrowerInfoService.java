package com.chen.srb.core.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface BorrowerInfoService {
    BigDecimal getBorrowAmount(Long userId);
}
