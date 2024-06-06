package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.Borrower;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BorrowerMapper {
    void insertBorrower(@Param("borrower")Borrower borrower);
}
