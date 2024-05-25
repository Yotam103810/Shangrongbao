package com.chen.srb.core.service.impl;

import com.chen.srb.core.pojo.dto.IntegralGradeDTO;
import com.chen.srb.core.pojo.entity.IntegralGrade;
import com.chen.srb.core.mapper.IntegerGradeMapper;
import com.chen.srb.core.service.IntegerGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntegerGradeServiceImpl implements IntegerGradeService {

    @Autowired
    IntegerGradeMapper integerGradeMapper;

    @Override
    public List<IntegralGrade> list() {
        return integerGradeMapper.list();
    }

    @Override
    public boolean removeById(Long id) {
        return integerGradeMapper.removeById(id);
    }

    @Override
    public boolean save(IntegralGradeDTO integralGradeDTO) {
        return integerGradeMapper.save(integralGradeDTO);
    }

    @Override
    public IntegralGrade getById(Long id) {
        return integerGradeMapper.getById(id);
    }

    @Override
    public boolean updateById(IntegralGradeDTO integralGradeDTO) {
        boolean result = integerGradeMapper.updateById(integralGradeDTO);
        return result;
    }
}
