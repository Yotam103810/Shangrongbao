package com.chen.srb.core.controller.admin;


import com.chen.common.exception.BusinessException;
import com.chen.common.result.R;
import com.chen.common.result.ResponseEnum;
import com.chen.srb.core.pojo.dto.IntegralGradeDTO;
import com.chen.srb.core.pojo.entity.IntegralGrade;
import com.chen.srb.core.service.IntegerGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "积分等级管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {
    @Autowired
    private IntegerGradeService integerGradeService;

    /**
     * 查询积分等级表
     * @return  List<IntegralGradeReturn>
     */
    @ApiOperation(value = "积分等级列表")
    @GetMapping("/list")
    public R  listAll(){
        List<IntegralGrade> list = integerGradeService.list();
        return R.ok().data("list",list);
    }


    /**
     * 根据id删除积分等级
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id删除积分等级",notes = "逻辑删除")
    @DeleteMapping("/delete/{id}")
    @ApiParam(value = "数据id", required = true, example = "100")
    public R removeById(@PathVariable Long id){

        boolean result = integerGradeService.removeById(id);
        if(result){
            return R.ok().message("删除成功");
        }else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation(value = "新增积分等级")
    @PostMapping("/save")
    public R save(@ApiParam(value = "积分等级对象" , required = true)
                      @RequestBody IntegralGradeDTO integralGradeDTO){
        //借款额度不能为空
        if(integralGradeDTO.getBorrowAmount() == null){
            throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        }
        boolean result = integerGradeService.save(integralGradeDTO);
        if(result){
            return R.ok().message("添加成功");
        }else{
            return R.error().message("添加失败");
        }
    }

    @ApiOperation(value = "根据id查询积分等级")
    @GetMapping("/get/{id}")
    public R getById(
                        @ApiParam(value = "数据id", required = true, example = "1")
                        @PathVariable Long id){
        IntegralGrade integralGradeReturn = integerGradeService.getById(id);
        if(integralGradeReturn != null){
            return R.ok().data("record",integralGradeReturn);
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation(value = "更新积分等级")
    @PutMapping("/update")
    public R updateById(
            @ApiParam(value = "积分等级对象", required = true)
            @RequestBody IntegralGradeDTO integralGradeDTO){
        boolean result = integerGradeService.updateById(integralGradeDTO);
        if(result){
            return R.ok().message("修改成功");
        }else {
            return R.error().message("修改失败");
        }
    }
}
