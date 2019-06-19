package com.aorise.companymeeting.base;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/29.
 */
public class TimeAreaUtil {
    private static TimeAreaUtil INSTANCE;
    public static String current_time;
    private static SimpleDateFormat sm;

    public static TimeAreaUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TimeAreaUtil();
        }
        Date date = new Date();
        sm = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
        current_time = sm.format(date);
        return INSTANCE;
    }


    public boolean isInAnotherTimeArea(String str_start_time, String str_end_time, String str_particular_start_time, String str_particular_end_time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        /** 时间转为string*/

        Date start_hour_date = simpleDateFormat.parse(str_start_time, new ParsePosition(0));
        Date particular_start_hour_date = simpleDateFormat.parse(str_particular_start_time, new ParsePosition(0));
        Date particular_end_hour_date = simpleDateFormat.parse(str_particular_end_time, new ParsePosition(0));
        Date end_hour_date = simpleDateFormat.parse(str_end_time, new ParsePosition(0));

        if (particular_start_hour_date.before(start_hour_date) && start_hour_date.before(particular_end_hour_date)) {
            LogT.d("在区域内");
            return true;
        }

        if (start_hour_date.before(particular_start_hour_date)
                && end_hour_date.after(particular_start_hour_date)) {
            LogT.d(" 新会议会包含之前存在的会议时间");
            return true;
        }
        return false;
    }

    //yyyy:MM:dd-HH:mm
    public int getMeettingRoomStatus(List<MeettingInfo> list) {
        int status = 0;
        Date now_date = sm.parse(current_time, new ParsePosition(0));
        for (MeettingInfo data : list) {
            String start_time = data.getChooseDate() + data.getStart_time();
            String end_time = data.getChooseDate() + data.getEnd_time();
            Date start_date = sm.parse(start_time, new ParsePosition(0));
            Date end_date = sm.parse(end_time, new ParsePosition(0));
            LogT.d(" 开始日期: " + start_date + " 结束日期: " + end_date + " 当前日期: " + now_date);
            if (now_date.before(start_date)) {
                LogT.d(" 当前日期在某会议开始日期之前!");
                status = 0;
            }

            if (now_date.after(start_date) && now_date.before(end_date)) {
                LogT.d(" 会议中! ");
                status = 1;
                break;
            }
            if (now_date.equals(start_date)) {
                LogT.d(" 会议已开始!");
                status = 1;
                break;
            }
            LogT.d(" 现在的状态是: 0为未开始，1为会议中------ " + status);
        }
        return status;
    }

    public MeettingInfo getMeettingInfo(List<MeettingInfo> list) {
        Date now_date = sm.parse(current_time, new ParsePosition(0));
        MeettingInfo meettingInfo = new MeettingInfo();
        for (MeettingInfo data : list) {
            String start_time = data.getChooseDate() + data.getStart_time();
            String end_time = data.getChooseDate() + data.getEnd_time();
            Date start_date = sm.parse(start_time, new ParsePosition(0));
            Date end_date = sm.parse(end_time, new ParsePosition(0));

            LogT.d(" 开始日期 " + start_date + " 结束日期 " + end_date + " 当前日期 " + now_date);
            if (now_date.before(start_date)) {
                LogT.d(" 当前日期在某会议开始日期之前!");
                meettingInfo = null;
            }

            if (now_date.after(start_date) && now_date.before(end_date)) {
                LogT.d(" 会议中!");
                meettingInfo = data;
                break;
            }
            if (now_date.equals(start_date)) {
                LogT.d(" 会议已开始 !");
                meettingInfo = data;
                break;
            }

        }
        return meettingInfo;
    }

    public MeettingInfo getNextMeettingInfo(List<MeettingInfo> list) {
        MeettingInfo meettingInfo = new MeettingInfo();
        Date now_date = sm.parse(current_time, new ParsePosition(0));
        for (MeettingInfo data : sort(list)) {
            String start_time = data.getChooseDate() + data.getStart_time();
            String end_time = data.getChooseDate() + data.getEnd_time();
            Date start_date = sm.parse(start_time, new ParsePosition(0));
            Date end_date = sm.parse(end_time, new ParsePosition(0));

            LogT.d(" 开始日期 " + start_date + " 结束日期 " + end_date + " 当前日期 " + now_date);
            if (now_date.before(start_date)) {
                LogT.d(" 当前日期在某会议开始日期之前!");
                meettingInfo = data;
                break;
            }

            if (now_date.after(start_date) && now_date.before(end_date)) {
                LogT.d(" 会议中!");
                meettingInfo = data;
                // break;
            }
            if (now_date.equals(start_date)) {
                LogT.d(" 会议已开始 !");
                meettingInfo = data;
                //  break;
            }

        }
        return meettingInfo;
    }

    public static List<MeettingInfo> sort(List<MeettingInfo> meettingInfos) {
        List<MeettingInfo> returnlist = new ArrayList<>();
        for (int i = 0; i < meettingInfos.size() - 1; i++) {//外层for循环的变量i用于控制内层排序范围，ints.length-1=排序次数
            for (int j = 0; j < meettingInfos.size() - 1 - i; j++) {//内层for循环用于通过两两比较，将范围内最大元素置于最右边，ints.length-1-i = 遍历数组元素的最大下标
                String start_time = meettingInfos.get(j).getChooseDate() + meettingInfos.get(j).getStart_time();
                String next_time = meettingInfos.get(j + 1).getChooseDate() + meettingInfos.get(j + 1).getStart_time();
                Date start_date = sm.parse(start_time, new ParsePosition(0));
                Date next_date = sm.parse(next_time, new ParsePosition(0));
                if (next_date.before(start_date)){
                    MeettingInfo meettingInfo = meettingInfos.get(j);
                    meettingInfos.set(j,meettingInfos.get(j+1));
                    meettingInfos.set(j+1,meettingInfo);
                }
            }
        }
        returnlist.addAll(meettingInfos);
        return returnlist;
    }
}
