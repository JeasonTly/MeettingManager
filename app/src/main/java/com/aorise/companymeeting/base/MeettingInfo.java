package com.aorise.companymeeting.base;

import android.databinding.BaseObservable;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/10.
 */
public class MeettingInfo extends BaseObservable {
    @Override
    public String toString() {
        return "MeettingInfo{" +
                "roomName='" + roomName + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", chooseDate='" + chooseDate + '\'' +
                ", content='" + content + '\'' +
                ", departmentInfo=" + departmentInfo +
                '}';
    }

    private String roomName;
    private String chooseDate;
    private String start_time;
    private String end_time;
    private String content;
    private String departmentInfo;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getChooseDate() {
        return chooseDate;
    }

    public void setChooseDate(String chooseDate) {
        this.chooseDate = chooseDate;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDepartmentInfo() {
        return departmentInfo;
    }

    public void setDepartmentInfo(String departmentInfo) {
        this.departmentInfo = departmentInfo;
    }
}
