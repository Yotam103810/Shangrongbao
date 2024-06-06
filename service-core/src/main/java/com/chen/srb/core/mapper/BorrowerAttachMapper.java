package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.BorrowerAttach;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowerAttachMapper {
    void insertBorrowerAttach(BorrowerAttach borrowerAttach);
}
