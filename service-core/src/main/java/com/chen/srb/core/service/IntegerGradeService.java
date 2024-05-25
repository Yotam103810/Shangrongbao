package com.chen.srb.core.service;

import com.chen.srb.core.pojo.dto.IntegralGradeDTO;
import com.chen.srb.core.pojo.entity.IntegralGrade;

import java.util.List;


public interface IntegerGradeService {


    List<IntegralGrade> list();

    boolean removeById(Long id);

    boolean save(IntegralGradeDTO integralGradeDTO);

    IntegralGrade getById(Long id);

    boolean updateById(IntegralGradeDTO integralGradeDTO);
}
