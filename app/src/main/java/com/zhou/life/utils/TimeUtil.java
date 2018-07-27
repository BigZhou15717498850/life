package com.zhou.life.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 作者 ly309313
 * 日期 2018/7/16
 * 描述
 */

public class TimeUtil {

    private static final SimpleDateFormat DATE_BILL = new SimpleDateFormat("MM-dd", Locale.CHINA);

    public static boolean overDate(String date){
        Date today = new Date();
        String today_str = DATE_BILL.format(today);
        return today_str.compareToIgnoreCase(date) > 0;
    }
}
