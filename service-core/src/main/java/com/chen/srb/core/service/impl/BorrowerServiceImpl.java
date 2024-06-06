package com.chen.srb.core.service.impl;

import com.chen.srb.core.enums.BorrowerStatusEnum;
import com.chen.srb.core.mapper.BorrowerAttachMapper;
import com.chen.srb.core.mapper.BorrowerMapper;
import com.chen.srb.core.mapper.UserInfoMapper;
import com.chen.srb.core.pojo.dto.BorrowerDTO;
import com.chen.srb.core.pojo.entity.Borrower;
import com.chen.srb.core.pojo.entity.BorrowerAttach;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.service.BorrowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BorrowerServiceImpl implements BorrowerService {

    @Autowired
    private BorrowerMapper borrowerMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private BorrowerAttachMapper borrowerAttachMapper;

    @Override
    public void saveBorrower(BorrowerDTO borrowerDTO, Long userId) {
        Borrower borrower = new Borrower();
        //从userInfo表中获取借款人信息 用户姓名 手机号  身份证
        UserInfo userInfo = userInfoMapper.selectUserById(userId);
        //保存borrower信息
        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        borrower.setSex(borrowerDTO.getSex());
        borrower.setAge(borrowerDTO.getAge());
        borrower.setEducation(borrowerDTO.getEducation());
        borrower.setMarry(borrowerDTO.getMarry());
        borrower.setIndustry(borrowerDTO.getIndustry());
        borrower.setIncome(borrowerDTO.getIncome());
        borrower.setReturnSource(borrowerDTO.getReturnSource());
        borrower.setContactsName(borrowerDTO.getContactsName());
        borrower.setContactsMobile(borrowerDTO.getContactsMobile());
        borrower.setContactsRelation(borrowerDTO.getContactsRelation());
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());

        borrowerMapper.insertBorrower(borrower);

        //保存图片信息
        List<BorrowerAttach> borrowerAttachList = borrowerDTO.getBorrowerAttachList();
        borrowerAttachList.forEach(borrowerAttach -> {
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insertBorrowerAttach(borrowerAttach);
        });

        //更新会员状态  borrow_auth_status
        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateUserInfo(userInfo);

    }
}
