package com.chen.srb.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.srb.core.pojo.dto.RegisterDTO;
import com.chen.srb.core.pojo.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {


    void register(UserInfo userInfo);

    int selectCount(@Param("mobile") String mobile);

    Long selectIdByMobile(@Param("mobile") String mobile);

    UserInfo selectUser(@Param("mobile") String mobile,@Param("password") String password,@Param("userType") Integer userType);


    void lock(@Param("id") Long id,@Param("status") Integer status);

    List<UserInfo> listPage(@Param("monile") String mobile,@Param("userType") Integer userType,@Param("status") Integer status);


    UserInfo selectUserById(@Param("id") Long valueOf);

    void updateUserInfo(UserInfo userInfo);

    void updateUserInfoIntegralAndBorrowAuthStatus(UserInfo userInfo);

    UserInfo selectUserInfoByBindCode(@Param("bindCode") String bindCode);
}
