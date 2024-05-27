package com.chen.srb.core.controller.admin;


import com.chen.common.result.R;
import com.chen.srb.core.pojo.vo.UserLoginRecordVO;
import com.chen.srb.core.service.UserLoginRecordService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/core/userLoginRecord")
@CrossOrigin
@Api(tags = "会员登录日志")
public class AdminUserLoginRecordController {

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    /**
     * 显示某个用户登录日志的前条
     * @param userId
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping("/listTop50/{userId}/{pageSize}/{pageNum}")
    public R listTop50(@PathVariable("userId") Long userId,
                       @PathVariable("pageSize") Integer pageSize,
                       @PathVariable("pageNum") Integer pageNum){
        UserLoginRecordVO userLoginRecordVO = userLoginRecordService.listTop50(userId,pageSize,pageNum);
        return R.ok().data("listTop50",userLoginRecordVO);
    }

}
