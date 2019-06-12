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
                ", meetting_content='" + meetting_content + '\'' +
                ", room_name='" + room_name + '\'' +
                ", inTheMeetting=" + inTheMeetting +
                '}';
    }

    private String name;
    private String room_name;
    private String meetting_content;
    private boolean inTheMeetting;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getMeetting_content() {
        return meetting_content;
    }

    public void setMeetting_content(String meetting_content) {
        this.meetting_content = meetting_content;
    }

    public boolean isInTheMeetting() {
        return inTheMeetting;
    }

    public void setInTheMeetting(boolean inTheMeetting) {
        this.inTheMeetting = inTheMeetting;
    }

}
