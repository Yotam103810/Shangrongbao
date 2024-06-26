package com.chen.srb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.common.result.R;
import com.chen.srb.core.pojo.dto.LoginDTO;
import com.chen.srb.core.pojo.dto.RegisterDTO;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.pojo.query.UserInfoQuery;
import com.chen.srb.core.pojo.vo.UserInfoVO;

public interface UserInfoService {
    void register(RegisterDTO registerDTO);

    UserInfoVO login(LoginDTO loginDTO, String ip);

    UserInfo listPage(Integer pageNum,Integer pageSize, UserInfoQuery userInfoQuery);

    void lock(Long id, Integer status);

    boolean checkMobile(String mobile);
}
