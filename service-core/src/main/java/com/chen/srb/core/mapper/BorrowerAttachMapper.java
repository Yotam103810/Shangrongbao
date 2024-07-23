package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.BorrowerAttach;
import com.chen.srb.core.pojo.vo.BorrowerAttachVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BorrowerAttachMapper {
    void insertBorrowerAttach(BorrowerAttach borrowerAttach);

    List<BorrowerAttachVO> selectBorrowerAttachVOList(@Param("borrowerId") Long id);
}
