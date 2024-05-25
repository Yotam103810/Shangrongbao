package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.UserLoginRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLoginRecordMapper {
    void insert(UserLoginRecord userLoginRecord);
}
