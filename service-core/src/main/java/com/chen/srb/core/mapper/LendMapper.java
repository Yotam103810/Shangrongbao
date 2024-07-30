package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.Lend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LendMapper {
    void insertLend(Lend lend);

    List<Lend> selectList();
}
