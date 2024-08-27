package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface UserAccountMapper {

    void insert(UserAccount userAccount);

    void updateAccount(@Param("bindCode") String bindCode,@Param("amount") BigDecimal bigDecimal,@Param("freezeAmount") BigDecimal bigDecimal1);
}
