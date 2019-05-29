package com.aorise.companymeeting.base;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/29.
 */
public class TimeAreaUtil {
    private static TimeAreaUtil INSTANCE;

    public static TimeAreaUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TimeAreaUtil();
        }
        return INSTANCE;
    }

    public boolean isInAnotherTimeArear(int start_Hour, int start_minutes,
                                        int end_hour, int end_minutes,
                                        int particular_start_hour, int particular_start_minuts,
                                        int particular_end_hour, int particular_end_minuts) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        /** 时间转为string*/
        String str_start_time = appendZero(start_Hour) + ":" + appendZero(start_minutes);
        String str_particular_start_time = appendZero(particular_start_hour) + ":" + appendZero(particular_start_minuts);
        String str_particular_end_time = appendZero(particular_end_hour) + ":" + appendZero(particular_end_minuts);
        String str_end_time = appendZero(end_hour) + ":" + appendZero(end_minutes);

        ParsePosition position = new ParsePosition(0);
        Date start_hour_date = simpleDateFormat.parse(str_start_time, position);
        Date particular_start_hour_date = simpleDateFormat.parse(str_particular_start_time, new ParsePosition(1));
        Date particular_end_hour_date = simpleDateFormat.parse(str_particular_end_time, new ParsePosition(1));
        Date end_hour_date = simpleDateFormat.parse(str_end_time,new ParsePosition(1));

        LogT.d(" 当前会议开始时间: " + str_start_time +
                " 会议列表中的某个会议开始时间: " + str_particular_start_time + " 会议列表中的某个会议结束时间: " + str_particular_end_time
        +" 当前会议结束时间： " +  str_end_time);

        LogT.d(" 某个会议开始时间在此会议开始时间之前? " + particular_start_hour_date.before(start_hour_date));
        LogT.d(" 此会议开始时间是否在某个会议结束之前? " + start_hour_date.before(particular_end_hour_date));
        if (particular_start_hour_date.before(start_hour_date) && start_hour_date.before(particular_end_hour_date)) {
            LogT.d("在区域内");
            return true;
        }
        LogT.d(" 此会议开始时间是否在会议列表中的某个会议开始时间之前? " + particular_start_hour_date.after(start_hour_date));
        LogT.d(" 会议列表中的某个会议的开始时间是否在新会议结束时间之后? " + end_hour_date.after(particular_start_hour_date));
        if (particular_start_hour_date.after(start_hour_date) && end_hour_date.after(particular_start_hour_date)){
            LogT.d("新会议会包含之前存在的会议时间");
            return true;
        }
            return false;
    }

    private String appendZero(int time) {
        if (time < 10) {
            return "0" + String.valueOf(time);
        } else {
            return String.valueOf(time);
        }
    }
}
