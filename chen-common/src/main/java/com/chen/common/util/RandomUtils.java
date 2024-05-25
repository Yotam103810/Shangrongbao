package com.chen.common.util;

import java.util.Random;


public class RandomUtils {

    public static String getFourBitRandom(){

        //生成四位随机数验证码并返回
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);// 生成1000到9999之间的随机数
        return String.valueOf(code);
    }
}
