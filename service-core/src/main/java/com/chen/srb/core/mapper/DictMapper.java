package com.chen.srb.core.mapper;

import com.chen.srb.core.pojo.entity.Dict;
import com.chen.srb.core.pojo.dto.ExcelDictDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictMapper {
    void insertBatch(@Param("list") List<ExcelDictDTO> list);

    List<Dict> listDictData();

    List<Dict> listByParentId(@Param("parentId") Long parentId);

    int selectCount(@Param("parentId") Long id);
}
