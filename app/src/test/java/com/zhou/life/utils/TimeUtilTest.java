package com.zhou.life.utils;

import org.junit.Test;

/**
 * 作者 ly309313
 * 日期 2018/7/16
 * 描述
 */

public class TimeUtilTest {

    @Test
    public void overBillDateTest(){
        String date = "16-16";

        boolean overed = TimeUtil.overDate(date);

        System.out.println("结果:==" + overed);
    }
}
