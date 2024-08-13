package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.Borrower;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BorrowerMapper {
    void insertBorrower(@Param("borrower")Borrower borrower);

    List<Object> getBorrowerStatus(@Param("userId") Long userId);

    List<Borrower> listPage(@Param("keyword") String keyword);

    Borrower getBorrowerDetail(@Param("id") Long id);

    void updateBorrowerStatus(Borrower borrowerDetail);

    Borrower getBorrowerDetailByUserId(@Param("userId") Long userId);
}
