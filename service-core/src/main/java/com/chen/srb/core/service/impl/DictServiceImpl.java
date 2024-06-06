package com.chen.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.CollectionUtils;
import com.chen.srb.core.pojo.entity.Dict;
import com.chen.srb.core.pojo.dto.ExcelDictDTO;
import com.chen.srb.core.listener.ExcelDictDTOListener;
import com.chen.srb.core.mapper.DictMapper;
import com.chen.srb.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    DictMapper dictMapper;

    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public void importData(InputStream inputStream) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(inputStream, ExcelDictDTO.class,new ExcelDictDTOListener(dictMapper)).sheet().doRead();
        log.info("importData finished");
    }


    @Override
    public List listDictData() {
        return dictMapper.listDictData();
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {
        //先从redis中取值
        List<Dict> dictList = null;
        try{
            dictList = (List<Dict>)redisTemplate.opsForValue().get("srb:core:dictList:" + parentId);
            if(dictList != null){
                log.info("从rides中取值");
                return dictList;
            }
        }catch (Exception e){
            log.error("redis服务器异常" + ExceptionUtils.getStackTrace(e));
        }
        log.info("redis中没有缓存，从数据库中查询");
        dictList = dictMapper.listByParentId(parentId);
        if(CollectionUtils.isEmpty(dictList)){  //判断为不为空
            return new ArrayList<Dict>();
        }
        dictList.forEach(dict -> {
            boolean hasChildren = this.hasChildren(parentId);
            dict.setHasChildren(hasChildren);
        });

        //将数据存入redis
        try {
            redisTemplate.opsForValue().set("srb:core:dictList:" + parentId,dictList,5, TimeUnit.MINUTES);
            log.info("数据存入redis");
        }catch (Exception e){
            log.error("redis服务器异常:" + ExceptionUtils.getStackTrace(e));  //此处不抛出异常，继续执行后面的代码
        }
        return dictList;
    }

    //判断有无子数据
    public boolean hasChildren(Long id){
        int count = dictMapper.selectCount(id);
        //如果数量大于0，说明有子节点
        if(count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Long dictId = dictMapper.getIdByDictCode(dictCode);
        List<Dict> list = dictMapper.listByParentId(dictId);
        return list;
    }
}
