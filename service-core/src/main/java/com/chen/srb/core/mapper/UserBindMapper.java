package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.UserBind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserBindMapper {


    UserBind selectUserBind(@Param("userId") Long userId);

    UserBind selectCount(@Param("idCard") String idCard);

    void insertUserBind(UserBind userBind);

    void updateUserBind(UserBind userBind);
}
