package com.chen.srb.core.service;

import com.chen.srb.core.pojo.entity.Dict;

import java.io.InputStream;
import java.util.List;

public interface DictService {
    void importData(InputStream inputStream);

    List listDictData();

    List<Dict> listByParentId(Long parentId);

    List<Dict> findByDictCode(String dictCode);
}
