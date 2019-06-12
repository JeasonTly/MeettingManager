package com.aorise.companymeeting.base;

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


    public boolean isInAnotherTimeArea(String str_start_time,String str_end_time ,String str_particular_start_time,String str_particular_end_time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        /** 时间转为string*/

        Date start_hour_date = simpleDateFormat.parse(str_start_time, new ParsePosition(0));
        Date particular_start_hour_date = simpleDateFormat.parse(str_particular_start_time, new ParsePosition(0));
        Date particular_end_hour_date = simpleDateFormat.parse(str_particular_end_time, new ParsePosition(0));
        Date end_hour_date = simpleDateFormat.parse(str_end_time, new ParsePosition(0));

//        LogT.d(" 当前会议开始时间: " + str_start_time +
//                " 会议列表中的某个会议开始时间: " + str_particular_start_time + " 会议列表中的某个会议结束时间: " + str_particular_end_time
//                + " 当前会议结束时间： " + str_end_time);
//
//        LogT.d(" 某个会议开始时间在此会议开始时间之前? " + particular_start_hour_date.before(start_hour_date));
//        LogT.d(" 此会议开始时间是否在某个会议结束之前? " + start_hour_date.before(particular_end_hour_date));
        if (particular_start_hour_date.before(start_hour_date) && start_hour_date.before(particular_end_hour_date)) {
            LogT.d("在区域内");
            return true;
        }
//        LogT.d(" 此会议开始时间是否在会议列表中的某个会议开始时间之前? " + start_hour_date.before(particular_start_hour_date));
//        LogT.d(" 此会议结束时间是否在会议列表中的某个会议开始时间之后? " + end_hour_date.after(particular_start_hour_date));
//
//        LogT.d(" end_hour_date " + end_hour_date.toString() + "\n particular_start_hour_date " + particular_start_hour_date.toString());
//        LogT.d(" start_hour_date " + start_hour_date.toString() + "\n particular_end_hour_date " + particular_end_hour_date.toString());

        if (start_hour_date.before(particular_start_hour_date)
                && end_hour_date.after(particular_start_hour_date)) {
            LogT.d(" 新会议会包含之前存在的会议时间");
            return true;
        }
        return false;
    }
    //yyyy:MM:dd-HH:mm
    public int getMeettingRoomStatus(List<MeettingInfo> list, String now) {
        int status = 0;
        for (MeettingInfo data : list) {
            String start_time = data.getChooseDate() + data.getStart_time();
            String end_time = data.getChooseDate() + data.getEnd_time();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
            Date start_date = dateFormat.parse(start_time, new ParsePosition(0));
            Date end_date = dateFormat.parse(end_time, new ParsePosition(0));
            Date now_date = dateFormat.parse(now, new ParsePosition(0));
            LogT.d(" start_date " + start_date + " end_date " + end_date + " now_date " + now_date);
            if (now_date.before(start_date)) {
                LogT.d("当前日期在某会议开始日期之前!");
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
    public MeettingInfo getMeettingInfo(List<MeettingInfo> list, String now) {
        MeettingInfo meettingInfo = new MeettingInfo();
        for (MeettingInfo data : list) {
            String start_time = data.getChooseDate() + data.getStart_time();
            String end_time = data.getChooseDate() + data.getEnd_time();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
            Date start_date = dateFormat.parse(start_time, new ParsePosition(0));
            Date end_date = dateFormat.parse(end_time, new ParsePosition(0));
            Date now_date = dateFormat.parse(now, new ParsePosition(0));
            LogT.d(" start_date " + start_date + " end_date " + end_date + " now_date " + now_date);
            if (now_date.before(start_date)) {
                LogT.d("当前日期在某会议开始日期之前!");
                meettingInfo = data;
            }

            if (now_date.after(start_date) && now_date.before(end_date)) {
                LogT.d(" now_date.after(start_date) && now_date.before(end_date) ");
                meettingInfo = data;
                break;
            }
            if(now_date.equals(start_date)){
                LogT.d(" now date.equal meetting start date");
                meettingInfo = data;
                break;
            }

        }
        return meettingInfo;
    }

}
