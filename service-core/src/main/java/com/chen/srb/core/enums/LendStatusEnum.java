package com.chen.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LendStatusEnum {
    CHECK(0,"待发布"),
    INVEST_RUN(1,"募资中"),
    PAY_RUN(2,"还款中"),
    PAY_OK(3,"已结清"),
    FINISH(4,"结标"),
    CANCEL(-1,"已撤标"),
    ;

    private Integer status;
    private String msg;

    public static String getMsgByStatus(int status) {
        LendStatusEnum arrObj[] = LendStatusEnum.values();
        for (LendStatusEnum obj : arrObj) {
            if (status == obj.getStatus().intValue()) {
                return obj.getMsg();
            }
        }
        return "";
    }
}
