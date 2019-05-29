package com.aorise.companymeeting.base;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/17.
 */
public class MeettingRomItem {
    private String name;
    private String location;
    private int todo_Count;
    private boolean inUse;

    public MeettingRomItem(String name, String location, int todo_Count, boolean inUse) {
        this.name = name;
        this.location = location;
        this.todo_Count = todo_Count;
        this.inUse = inUse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTodo_Count() {
        return todo_Count;
    }

    public void setTodo_Count(int todo_Count) {
        this.todo_Count = todo_Count;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

}
