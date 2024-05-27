package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.UserLoginRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserLoginRecordMapper {
    void insert(UserLoginRecord userLoginRecord);

    List<UserLoginRecord> listTop50(@Param("userId") Long userId);
}
