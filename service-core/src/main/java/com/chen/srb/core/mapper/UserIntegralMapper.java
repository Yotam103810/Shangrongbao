package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.UserIntegral;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserIntegralMapper {
    void insertUserIntegral(UserIntegral userIntegral);
}
