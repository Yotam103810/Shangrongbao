package com.chen.srb.core.service;

import com.chen.srb.core.pojo.dto.LoginDTO;
import com.chen.srb.core.pojo.dto.RegisterDTO;
import com.chen.srb.core.pojo.vo.UserInfoVO;

public interface UserInfoService {
    void register(RegisterDTO registerDTO);

    UserInfoVO login(LoginDTO loginDTO, String ip);
}
