package com.aorise.companymeeting.time;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/17.
 */
public class MeetingTimeTask {
    private String startTime;
    private String endTime;
    private boolean inUse;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
}
