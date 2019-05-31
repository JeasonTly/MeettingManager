package com.aorise.companymeeting.base;

import android.databinding.BaseObservable;


/**
 * Created by Tuliyuan.
 * Date: 2019/5/28.
 */
public class MeettingContent extends BaseObservable {
    @Override
    public String toString() {
        return "MeettingContent{" +
                "start_year='" + start_year + '\'' +
                ", start_month='" + start_month + '\'' +
                ", start_day='" + start_day + '\'' +
                ", start_hour='" + start_hour + '\'' +
                ", start_minutes='" + start_minutes + '\'' +
                ", end_hour='" + end_hour + '\'' +
                ", end_minutes='" + end_minutes + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    private String room_name ;

    private String start_year;

    private String start_month;

    private String start_day;

    private String start_hour;

    private String start_minutes;

    private String end_hour;

    private String end_minutes;

    private String content;

    public MeettingContent(String start_year, String start_month, String start_day, String start_hour, String start_minutes,
                           String end_hour, String end_minutes) {
        this.start_year = start_year;
        this.start_month = start_month;
        this.start_day = start_day;
        this.start_hour = start_hour;
        this.start_minutes = start_minutes;

        this.end_hour = end_hour;
        this.end_minutes = end_minutes;
    }

    public String getStart_year() {
        return start_year;
    }

    public void setStart_year(String start_year) {
        this.start_year = start_year;
    }

    public String getStart_month() {
        return start_month;
    }

    public void setStart_month(String start_month) {
        this.start_month = start_month;
    }

    public String getStart_day() {
        return start_day;
    }

    public void setStart_day(String start_day) {
        this.start_day = start_day;
    }

    public String getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }

    public String getStart_minutes() {
        return start_minutes;
    }

    public void setStart_minutes(String start_minutes) {
        this.start_minutes = start_minutes;
    }

    public String getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }

    public String getEnd_minutes() {
        return end_minutes;
    }

    public void setEnd_minutes(String end_minutes) {
        this.end_minutes = end_minutes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRoomName() {
        return room_name;
    }

    public void setRoomName(String room_name) {
        this.room_name = room_name;
    }

}
