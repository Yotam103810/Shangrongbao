package com.chen.srb.core.service;

import com.chen.srb.core.pojo.vo.UserLoginRecordVO;

public interface UserLoginRecordService {
    UserLoginRecordVO listTop50(Long userId, Integer pageSize, Integer pageNum);
}
