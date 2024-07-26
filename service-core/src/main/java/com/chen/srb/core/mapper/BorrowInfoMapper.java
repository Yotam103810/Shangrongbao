package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.BorrowerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BorrowInfoMapper {
    void insertBorrowInfo(BorrowerInfo borrowerInfo);

    List<Object> getBorrowerStatus(@Param("userId") Long userId);

    List<BorrowerInfo> list();

    BorrowerInfo selectBorrowInfoById(@Param("id") Long id);

    void updateBorrowInfoStatus(BorrowerInfo borrowerInfo);
}
