package com.chen.srb.core.service.impl;

import com.chen.srb.core.mapper.UserLoginRecordMapper;
import com.chen.srb.core.pojo.entity.UserLoginRecord;
import com.chen.srb.core.pojo.vo.UserLoginRecordVO;
import com.chen.srb.core.service.UserLoginRecordService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserLoginRecordServiceImpl implements UserLoginRecordService {

    @Autowired
    UserLoginRecordMapper userLoginRecordMapper;
    @Override
    public UserLoginRecordVO listTop50(Long userId, Integer pageSize, Integer pageNum) {
        List<UserLoginRecord> list = userLoginRecordMapper.listTop50(userId);

        List<List<UserLoginRecord>> partition = Lists.partition(list, pageSize);
        UserLoginRecordVO userLoginRecordVO = new UserLoginRecordVO();
        userLoginRecordVO.setPageSize(pageSize);
        userLoginRecordVO.setPageNum(pageNum);
        userLoginRecordVO.setTotalCount(list.size());
        userLoginRecordVO.setPageTotal(partition.size());
        userLoginRecordVO.setUserLoginRecordList(partition.get(pageNum-1));

        return userLoginRecordVO;
    }
}
