package com.chen.srb.core.service;

import java.math.BigDecimal;
import java.util.Map;

public interface UserAccountService {
    String commitCharge(BigDecimal chargeAmt, Long userId);

    String notify(Map<String, Object> map);
}
