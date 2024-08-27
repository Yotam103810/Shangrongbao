package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.TransFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TransFlowMapper {
    void insert(TransFlow transFlow);

    int selectTransFlow(@Param("transNo") String agentBillNo);
}
