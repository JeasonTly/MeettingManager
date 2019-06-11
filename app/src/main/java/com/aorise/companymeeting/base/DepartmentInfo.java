package com.aorise.companymeeting.base;

import java.io.Serializable;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/10.
 */
public class DepartmentInfo implements Serializable {
    @Override
    public String toString() {
        return "DepartmentInfo{" +
                "name='" + name + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", room_name='" + room_name + '\'' +
                ", inTheMeetting=" + inTheMeetting +
                '}';
    }

    private String name;
    private String start_time;
    private String end_time;
    private String room_name;
    private boolean inTheMeetting;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public boolean isInTheMeetting() {
        return inTheMeetting;
    }

    public void setInTheMeetting(boolean inTheMeetting) {
        this.inTheMeetting = inTheMeetting;
    }

}
