package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.Lend;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LendMapper {
    void insertLend(Lend lend);
}
