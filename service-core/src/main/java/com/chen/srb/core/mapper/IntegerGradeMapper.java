package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.dto.IntegralGradeDTO;
import com.chen.srb.core.pojo.entity.IntegralGrade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IntegerGradeMapper {

    List<IntegralGrade> list();

    boolean removeById(@Param("id") Long id);

    boolean save(IntegralGradeDTO integralGradeDTO);

    IntegralGrade getById(@Param("id") Long id);

    boolean updateById(IntegralGradeDTO integralGradeDTO);
}
