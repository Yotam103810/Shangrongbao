package com.chen.srb.core.service;

import com.chen.srb.core.pojo.bo.TransFlowBO;

public interface TransFlowService {
    void saveTransFlow(TransFlowBO transFlowBO);

    boolean isSaveTransFlow(String agentBillNo);
}
