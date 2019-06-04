package com.aorise.companymeeting.base;

import com.haibin.calendarview.Calendar;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public boolean isInAnotherTimeArea(int start_Hour, int start_minutes,
                                       int end_hour, int end_minutes,
                                       int particular_start_hour, int particular_start_minuts,
                                       int particular_end_hour, int particular_end_minuts) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        /** 时间转为string*/
        String str_start_time = appendZero(start_Hour) + ":" + appendZero(start_minutes);
        String str_particular_start_time = appendZero(particular_start_hour) + ":" + appendZero(particular_start_minuts);
        String str_particular_end_time = appendZero(particular_end_hour) + ":" + appendZero(particular_end_minuts);
        String str_end_time = appendZero(end_hour) + ":" + appendZero(end_minutes);

        Date start_hour_date = simpleDateFormat.parse(str_start_time, new ParsePosition(0));
        Date particular_start_hour_date = simpleDateFormat.parse(str_particular_start_time, new ParsePosition(0));
        Date particular_end_hour_date = simpleDateFormat.parse(str_particular_end_time, new ParsePosition(0));
        Date end_hour_date = simpleDateFormat.parse(str_end_time, new ParsePosition(0));

        LogT.d(" 当前会议开始时间: " + str_start_time +
                " 会议列表中的某个会议开始时间: " + str_particular_start_time + " 会议列表中的某个会议结束时间: " + str_particular_end_time
                + " 当前会议结束时间： " + str_end_time);

        LogT.d(" 某个会议开始时间在此会议开始时间之前? " + particular_start_hour_date.before(start_hour_date));
        LogT.d(" 此会议开始时间是否在某个会议结束之前? " + start_hour_date.before(particular_end_hour_date));
        if (particular_start_hour_date.before(start_hour_date) && start_hour_date.before(particular_end_hour_date)) {
            LogT.d("在区域内");
            return true;
        }
        LogT.d(" 此会议开始时间是否在会议列表中的某个会议开始时间之前? " + start_hour_date.before(particular_start_hour_date));
        LogT.d(" 此会议结束时间是否在会议列表中的某个会议开始时间之后? " + end_hour_date.after(particular_start_hour_date));

        LogT.d(" end_hour_date " + end_hour_date.toString() + "\n particular_start_hour_date " + particular_start_hour_date.toString());
        LogT.d(" start_hour_date " + start_hour_date.toString() + "\n particular_end_hour_date " + particular_end_hour_date.toString());

        if (start_hour_date.before(particular_start_hour_date)
                && end_hour_date.after(particular_start_hour_date)) {
            LogT.d(" 新会议会包含之前存在的会议时间");
            return true;
        }
        return false;
    }

    //yyyy:MM:dd-HH:mm
    public int getMeettingRoomStatus(List<MeettingContent> list, String now) {
        int status = 0;
        for (MeettingContent data : list) {
            String start_time = data.getStart_year() + ":" + data.getStart_month() + ":" + data.getStart_day() + "-" + data.getStart_hour() + ":" + data.getStart_minutes();
            String end_time = data.getStart_year() + ":" + data.getStart_month() + ":" + data.getStart_day() + "-" + data.getEnd_hour() + ":" + data.getEnd_minutes();
            LogT.d(" start_time is " + start_time + " end time is " + end_time);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd-HH:mm");
            Date start_date = dateFormat.parse(start_time, new ParsePosition(0));
            Date end_date = dateFormat.parse(end_time, new ParsePosition(0));
            Date now_date = dateFormat.parse(now, new ParsePosition(0));
            LogT.d(" start_date " + start_date + " end_date " + end_date + " now_date " + now_date);
            if (now_date.before(start_date)) {
                LogT.d(" now_date.before(start_date) ");
                status = 0;
            }

            if (now_date.after(start_date) && now_date.before(end_date)) {
                LogT.d(" now_date.after(start_date) && now_date.before(end_date) ");
                status = 1;
                break;
            }
            if(now_date.equals(start_date)){
                LogT.d(" now date.equal meetting start date");
                status = 1;
                break;
            }
            LogT.d(" now status is " + status);
        }
        return status;
    }

    private String appendZero(int time) {
        if (time < 10) {
            return "0" + String.valueOf(time);
        } else {
            return String.valueOf(time);
        }
    }
}
