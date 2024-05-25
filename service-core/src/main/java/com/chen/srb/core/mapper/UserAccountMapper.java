package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAccountMapper {

    void insert(UserAccount userAccount);
}
