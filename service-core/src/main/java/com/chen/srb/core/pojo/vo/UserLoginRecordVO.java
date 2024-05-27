package com.chen.srb.core.pojo.vo;

import com.chen.srb.core.pojo.entity.UserLoginRecord;
import lombok.Data;

import java.util.List;

@Data
public class UserLoginRecordVO {

    //当前页
    private Integer pageNum;

    //每页的条数
    private Integer pageSize;

    //总共多少条数据
    private Integer totalCount;

    //总共多少页
    private Integer pageTotal;

    //当前分页对象
    private List<UserLoginRecord> userLoginRecordList;


}
