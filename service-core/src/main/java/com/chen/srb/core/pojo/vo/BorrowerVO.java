package com.chen.srb.core.pojo.vo;

import com.chen.srb.core.pojo.entity.Borrower;
import lombok.Data;

import java.util.List;

@Data
public class BorrowerVO {

    //当前页
    private Integer pageNum;

    //每页数量
    private Integer pageSize;

    //总页数
    private Integer pageTotal;

    //总条数
    private Integer totalSize;

    private List<Borrower> borrowerList;


}
