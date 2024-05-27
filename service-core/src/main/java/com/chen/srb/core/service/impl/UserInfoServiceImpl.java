package com.chen.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.common.exception.Assert;
import com.chen.common.exception.BusinessException;
import com.chen.common.result.ResponseEnum;
import com.chen.common.util.MD5Util;
import com.chen.srb.base.util.JwtUtils;
import com.chen.srb.core.mapper.UserLoginRecordMapper;
import com.chen.srb.core.pojo.dto.LoginDTO;
import com.chen.srb.core.pojo.dto.RegisterDTO;
import com.chen.srb.core.mapper.UserAccountMapper;
import com.chen.srb.core.mapper.UserInfoMapper;
import com.chen.srb.core.pojo.entity.UserAccount;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.pojo.entity.UserLoginRecord;
import com.chen.srb.core.pojo.query.UserInfoQuery;
import com.chen.srb.core.pojo.vo.UserInfoVO;
import com.chen.srb.core.service.UserInfoService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private UserLoginRecordMapper userLoginRecordMapper;

    @Resource
    private BaseMapper baseMapper;

    @Override
    public void register(RegisterDTO registerDTO) {

        //判断是否注册过
        int count = userInfoMapper.selectCount(registerDTO.getMobile());
        if(count > 0){
            throw new  BusinessException(ResponseEnum.MOBILE_EXIST_ERROR);
        }
        //添加用户基本信息
        String md5 = MD5Util.getMD5(registerDTO.getPassword());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserType(registerDTO.getUserType());
        userInfo.setNickName(registerDTO.getMobile());
        userInfo.setName(registerDTO.getMobile());
        userInfo.setMobile(registerDTO.getMobile());
        userInfo.setPassword(md5);
        userInfo.setStatus(UserInfo.STATUS_NORMAL); //正常
        userInfo.setHeadImg(UserInfo.USER_AVATAR);
        userInfoMapper.register(userInfo);

        Long userInfoId = userInfoMapper.selectIdByMobile(registerDTO.getMobile());
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfoId);
        //注册的同时，会员创建
        userAccountMapper.insert(userAccount);
    }

    @Override
    public UserInfoVO login(LoginDTO loginDTO, String ip) {
        Integer userType = loginDTO.getUserType();
        String password = loginDTO.getPassword();
        String mobile = loginDTO.getMobile();

        //从用户表中查询
        UserInfo userInfo = userInfoMapper.selectUser(mobile,MD5Util.getMD5(password),userType);
        Assert.notNull(userInfo,ResponseEnum.LOGIN_MOBILE_ERROR);
        //判断该用户是否被禁用
        Assert.equals(userInfo.getStatus(),UserInfo.STATUS_NORMAL,ResponseEnum.LOGIN_LOKED_ERROR);
        //校验密码
        Assert.equals(userInfo.getPassword(),MD5Util.getMD5(password),ResponseEnum.LOGIN_PASSWORD_ERROR);

        //同步登记表
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);

        //制造Token，封装到UserInfoVO中并返回
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName());
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setName(userInfo.getName());
        userInfoVO.setNickName(userInfo.getNickName());
        userInfoVO.setHeadImg(userInfo.getHeadImg());
        userInfoVO.setMobile(userInfo.getMobile());
        userInfoVO.setToken(token);
        userInfoVO.setUserType(userType);

        return userInfoVO;
    }

    @Override
    public UserInfo listPage(Integer pageNum,Integer pageSize, UserInfoQuery userInfoQuery) {
        String mobile = userInfoQuery.getMobile();
        Integer userType = userInfoQuery.getUserType();
        Integer status = userInfoQuery.getStatus();
        UserInfo userInfo = new UserInfo();
        //判断查询条件为不为空
        if(userInfoQuery == null){
             return userInfo;
        }

        List<UserInfo> list = userInfoMapper.listPage(mobile,userType,status);
        /*
         此方法为集合分片：
         pageSize: 前端传参（每页数量）
          Lists.partition：按照每页数量将集合进行分片，一个共多少片就是一共有多少页partition.size()
         */
        List<List<UserInfo>> partition = Lists.partition(list, pageSize);
        // 当前页
        userInfo.setPageSize(pageSize);
        userInfo.setPageNum(pageNum);
        userInfo.setPageTotal(partition.size());
        userInfo.setTotalCount(list.size());
        // 因为partition是集合，集合的第一个指针是0  所以要-1
        userInfo.setUserInfoList(partition.get(pageNum-1));
        return userInfo;


    }

    @Override
    public void lock(Long id, Integer status) {
        userInfoMapper.lock(id,status);
    }
}
