package com.chen.srb.core.service;

import com.chen.srb.core.pojo.entity.BorrowerInfo;
import com.chen.srb.core.pojo.entity.Lend;
import com.chen.srb.core.pojo.vo.BorrowInfoApprovalVO;

import java.util.List;

public interface LendService {
    void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowerInfo borrowerInfo);

    List<Lend> selectList();
}
