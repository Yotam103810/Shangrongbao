package com.chen.srb.core.service.impl;

import com.alibaba.excel.util.CollectionUtils;
import com.chen.srb.core.enums.BorrowerStatusEnum;
import com.chen.srb.core.enums.IntegralEnum;
import com.chen.srb.core.mapper.*;
import com.chen.srb.core.pojo.dto.BorrowerDTO;
import com.chen.srb.core.pojo.entity.Borrower;
import com.chen.srb.core.pojo.entity.BorrowerAttach;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.pojo.entity.UserIntegral;
import com.chen.srb.core.pojo.vo.BorrowerApprovalVO;
import com.chen.srb.core.pojo.vo.BorrowerAttachVO;
import com.chen.srb.core.pojo.vo.BorrowerDetailVO;
import com.chen.srb.core.pojo.vo.BorrowerVO;
import com.chen.srb.core.service.BorrowerService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BorrowerServiceImpl implements BorrowerService {

    @Autowired
    private BorrowerMapper borrowerMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private BorrowerAttachMapper borrowerAttachMapper;

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private UserIntegralMapper userIntegralMapper;

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
        borrower.setIsMarry(borrowerDTO.getMarry() ? 1 : 0);
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

    @Override
    public Integer getBorrowerStatus(Long userId) {
        List<Object> statusList = borrowerMapper.getBorrowerStatus(userId);
        if(statusList.size() == 0){
            //没有借款人信息
            return BorrowerStatusEnum.NO_AUTH.getStatus();
        }
        Integer status = (Integer) statusList.get(0);
        return status;
    }

    @Override
    public BorrowerVO listPage(Integer page, Integer limit, String keyword) {
        BorrowerVO borrowerVO = new BorrowerVO();
        List<Borrower> list = borrowerMapper.listPage(keyword);
        if(CollectionUtils.isEmpty(list)){
            return borrowerVO;
        }
        List<List<Borrower>> partition = Lists.partition(list, limit);
        borrowerVO.setPageSize(limit);  //每页的条数
        borrowerVO.setPageNum(page);   //当前页
        borrowerVO.setPageTotal(partition.size());  //总页数
        borrowerVO.setTotalSize(list.size());  //总条数
        borrowerVO.setBorrowerList(partition.get(page - 1));
        return borrowerVO;
    }


    @Override
    @Transactional
    public BorrowerDetailVO getBorrowerDetailVOById(Long id) {
        log.info("**************方法入参，{}",id);
        BorrowerDetailVO borrowerDetailVO = null;
        try {

            if (ObjectUtils.isEmpty(id) || id < 0){
                return new BorrowerDetailVO();
            }
            //根据借款人id查询
            Borrower borrower = borrowerMapper.getBorrowerDetail(id);
            if (ObjectUtils.isEmpty(borrower)){
                return new BorrowerDetailVO();
            }
            //将查询的借款人信息传递给借款人信息VO对象
            borrowerDetailVO = new BorrowerDetailVO();
            BeanUtils.copyProperties(borrower,borrowerDetailVO);

            //将借款人信息中的数字替换成对应的描述
            //性别
            // Sex -1
            borrowerDetailVO.setSex(borrower.getSex() == 1 ? "男" : "女");  //integer
            //是否结婚
            borrowerDetailVO.setMarry(borrower.getIsMarry() == 1 ? "是" : "否");  //boolean

            //计算下拉列表选中内容
            String education = dictMapper.getEducation(borrower.getEducation());   //学历
            String industry = dictMapper.getIndustry(borrower.getIndustry());    //行业
            String income = dictMapper.getIncome(borrower.getIncome());  //月收入
            String returnSource = dictMapper.getReturnSource(borrower.getReturnSource());  //还款来源
            String contactsRelation = dictMapper.getContactsRelation(borrower.getContactsRelation());  //联系人关系

            ////设置下拉列表选中内容
            borrowerDetailVO.setEducation(education);
            borrowerDetailVO.setIndustry(industry);
            borrowerDetailVO.setIncome(income);
            borrowerDetailVO.setReturnSource(returnSource);
            borrowerDetailVO.setContactsRelation(contactsRelation);

            //审批状态
            String msgByStatus = BorrowerStatusEnum.getMsgByStatus(borrower.getStatus());
            borrowerDetailVO.setStatus(msgByStatus);

            //获取附件的vo列表 String ''  , INt -1   ，Boolean false
            List<BorrowerAttachVO> borrowerAttachList = borrowerAttachMapper.selectBorrowerAttachVOList(id);
            borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachList);
        } catch (Exception e) {
            log.error("*********失败，{}",id,e);
            throw new RuntimeException(e);
        }
        return borrowerDetailVO;
    }

    @Override
    @Transactional
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {

        //同步认证状态
        Borrower borrowerDetail = borrowerMapper.getBorrowerDetail(borrowerApprovalVO.getBorrowerId());
        borrowerDetail.setStatus(borrowerApprovalVO.getStatus());
        borrowerMapper.updateBorrowerStatus(borrowerDetail);

        //获取用户，修改用户积分记录表和用户基本信息表中的积分
        Long userId = borrowerDetail.getUserId();
        UserInfo userInfo = userInfoMapper.selectUserById(userId);


        //添加用户基本信息积分到用户积分记录表中
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userId);
        userIntegral.setIntegral(borrowerApprovalVO.getInfoIntegral());
        userIntegral.setContent("用户基本信息积分");
        userIntegralMapper.insertUserIntegral(userIntegral);

        int curIntegral = userInfo.getIntegral() + borrowerApprovalVO.getInfoIntegral();
        //身份证信息是否正确
        if(borrowerApprovalVO.getIsIdCardOk()){
            curIntegral += IntegralEnum.BORROWER_IDCARD.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_IDCARD.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
            userIntegralMapper.insertUserIntegral(userIntegral);
        }

        //房产信息是否正确
        if(borrowerApprovalVO.getIsHouseOk()){
            curIntegral += IntegralEnum.BORROWER_HOUSE.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_HOUSE.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
            userIntegralMapper.insertUserIntegral(userIntegral);
        }

        //车辆信息是否正确
        if(borrowerApprovalVO.getIsCarOk()){
            curIntegral += IntegralEnum.BORROWER_CAR.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_CAR.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
            userIntegralMapper.insertUserIntegral(userIntegral);
        }

        //修改用户基本信息表中的积分
        userInfo.setIntegral(curIntegral);
        //修改用户的审核状态
        userInfo.setBorrowAuthStatus(borrowerApprovalVO.getStatus());
        userInfoMapper.updateUserInfoIntegralAndBorrowAuthStatus(userInfo);
    }
}
