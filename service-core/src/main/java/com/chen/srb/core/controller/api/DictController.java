package com.chen.srb.core.controller.api;

import com.chen.common.result.R;
import com.chen.srb.core.pojo.entity.Dict;
import com.chen.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/core/dict")
@Api(tags = "数据字典")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * 借款人填写个人信息，下拉返回字典数据
     * @param dictCode
     * @return
     */
    @ApiOperation("根据dictCode获取下级节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public R findByDictCode(@PathVariable("dictCode") String dictCode){
        List<Dict> list = dictService.findByDictCode(dictCode);
        return R.ok().data("dictList",list);
    }

}
