package com.chen.srb.core.service;

import com.chen.srb.core.pojo.dto.UserBindDTO;

import java.util.Map;

public interface UserBindService {
    String commitBindUser(UserBindDTO userBindDTO, Long userId);

    void notify(Map<String, Object> paramMap);
}
