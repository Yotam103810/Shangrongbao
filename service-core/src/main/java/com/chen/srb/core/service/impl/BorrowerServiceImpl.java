package com.chen.srb.core.service.impl;

import com.alibaba.excel.util.CollectionUtils;
import com.chen.srb.core.enums.BorrowerStatusEnum;
import com.chen.srb.core.mapper.BorrowerAttachMapper;
import com.chen.srb.core.mapper.BorrowerMapper;
import com.chen.srb.core.mapper.DictMapper;
import com.chen.srb.core.mapper.UserInfoMapper;
import com.chen.srb.core.pojo.dto.BorrowerDTO;
import com.chen.srb.core.pojo.entity.Borrower;
import com.chen.srb.core.pojo.entity.BorrowerAttach;
import com.chen.srb.core.pojo.entity.UserInfo;
import com.chen.srb.core.pojo.vo.BorrowerApprovalVO;
import com.chen.srb.core.pojo.vo.BorrowerAttachVO;
import com.chen.srb.core.pojo.vo.BorrowerDetailVO;
import com.chen.srb.core.pojo.vo.BorrowerVO;
import com.chen.srb.core.service.BorrowerService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private DictMapper dictMapper;

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
    public BorrowerDetailVO getBorrowerDetailVOById(Long id) {
        //根据借款人id查询
        Borrower borrower = borrowerMapper.getBorrowerDetail(id);

        //将查询的借款人信息传递给借款人信息VO对象
        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        BeanUtils.copyProperties(borrower,borrowerDetailVO);

        //将借款人信息中的数字替换成对应的描述
        //性别
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

        //获取附件的vo列表
        List<BorrowerAttachVO> borrowerAttachList = borrowerAttachMapper.selectBorrowerAttachVOList(id);
        borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachList);
        return borrowerDetailVO;
    }

    @Override
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {

        //同步认证状态
        Borrower borrowerDetail = borrowerMapper.getBorrowerDetail(borrowerApprovalVO.getBorrowerId());
        borrowerDetail.setStatus(borrowerApprovalVO.getStatus());
        borrowerMapper.updateBorrowerStatus(borrowerDetail);
    }
}
