package com.chen.srb.core.service;

import com.chen.srb.core.pojo.entity.BorrowerInfo;
import com.chen.srb.core.pojo.vo.BorrowInfoApprovalVO;

public interface LendService {
    void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowerInfo borrowerInfo);
}
