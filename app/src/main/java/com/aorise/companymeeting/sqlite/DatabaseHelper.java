package com.aorise.companymeeting.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aorise.companymeeting.base.DepartmentInfo;
import com.aorise.companymeeting.base.LogT;
import com.aorise.companymeeting.base.MeettingContent;
import com.aorise.companymeeting.base.MeettingInfo;
import com.aorise.companymeeting.base.MeettingRomItem;
import com.haibin.calendarview.Calendar;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/28.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "meettingcontent.db";
    private static final String TABLE_NAME = "MeettingTime";
    private static final String TABLE_NAME_ROOM = "MeettingRoomInfo";
    private static final String TABLE_NAME_DEPARTMENT = "Department";

    private SimpleDateFormat dateFormat;
    private static Date date;
    private static DatabaseHelper INSTANCE;

    public static DatabaseHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseHelper(context);
        }
        date = new Date();
        return INSTANCE;
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        dateFormat = new SimpleDateFormat("yyyy年MM月dd日hh:mm");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_NAME + " (Id integer primary key autoincrement, room_name text , chooseDate text , Start_Time text,End_Time text," +
                "MeettingContent text , DepartMentInfo text)";
        String create_room = "create table if not exists " + TABLE_NAME_ROOM + " (Id integer primary key autoincrement, room_name text, room_status integer, room_event integer)";

        String create_department = "create table if not exists " + TABLE_NAME_DEPARTMENT + " (Id integer primary key autoincrement, " +
                "depart_name text, Start_Time text  ,End_Time text , depart_room_name text, inMeetting integer)";

        db.execSQL(sql);
        db.execSQL(create_room);
        db.execSQL(create_department);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String sql1 = "DROP TABLE IF EXISTS " + TABLE_NAME_ROOM;
        String sql2 = "DROP TABLE IF EXISTS " + TABLE_NAME_DEPARTMENT;
        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
        onCreate(db);

    }

    public void insertMeetting(MeettingInfo content) {
        Log.d("DatabaseHelper", " insertMeetting content is " + content.toString());
        ContentValues contentValues = new ContentValues();
        contentValues.put("Start_Time", content.getStart_time());
        contentValues.put("End_Time", content.getEnd_time());
        contentValues.put("MeettingContent", content.getContent());
        contentValues.put("room_name", content.getRoomName());
        contentValues.put("DepartMentInfo", content.getDepartmentInfo());
        contentValues.put("chooseDate", content.getChooseDate());
        getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }

    public List<MeettingInfo> queryDayofMeetting(String roomName, Calendar calendar) {
        String year = appendZero(calendar.getYear());
        String month = appendZero(calendar.getMonth());
        String day = appendZero(calendar.getDay());
        String chooseDate = year + "年" + month + "月" + day + "日";
        List<MeettingInfo> meettingContentList = new ArrayList<>();
        Cursor cursor = getWritableDatabase().query(TABLE_NAME, null,
                "room_name = ? and chooseDate = ?", new String[]{roomName,
                        chooseDate},
                null, null, null, null);
        LogT.d(" cursor count " + cursor.getCount());
        // 不断移动光标获取值
        while (cursor.moveToNext()) {
            String start_time = cursor.getString(cursor.getColumnIndex("Start_Time"));
            String end_time = cursor.getString(cursor.getColumnIndex("End_Time"));
            String content = cursor.getString(cursor.getColumnIndex("MeettingContent"));
            String room_name = cursor.getString(cursor.getColumnIndex("room_name"));
            String dInfo = cursor.getString(cursor.getColumnIndex("DepartMentInfo"));
            String date = cursor.getString(cursor.getColumnIndex("chooseDate"));
            MeettingInfo meettingInfo = new MeettingInfo();
            meettingInfo.setStart_time(start_time);
            meettingInfo.setEnd_time(end_time);
            meettingInfo.setChooseDate(date);
            meettingInfo.setRoomName(room_name);
            meettingInfo.setContent(content);
            meettingInfo.setDepartmentInfo(dInfo);
            Log.d("DatabaseHelper", " meettingInfo is  " + meettingInfo);
            meettingContentList.add(meettingInfo);
        }
        // 关闭光标
        cursor.close();
        return meettingContentList;
    }

    /**
     * 用在MainActivity的refresh函数和onresume中，刷新数据时设置会议室的状态和待办事项，不添加已过期会议
     *
     * @param roomName
     * @return
     */
    public List<MeettingInfo> queryAllDayofMeetting(String roomName) {
        List<MeettingInfo> meettingContentList = new ArrayList<>();
        Cursor cursor = getWritableDatabase().query(TABLE_NAME, null,
                "room_name = ?", new String[]{roomName},
                null, null, null, null);
        LogT.d(" cursor count " + cursor.getCount());
        while (cursor.moveToNext()) {
            // 先获取 name 的索引值，然后再通过索引获取字段值
            String start_time = cursor.getString(cursor.getColumnIndex("Start_Time"));
            String end_time = cursor.getString(cursor.getColumnIndex("End_Time"));
            String content = cursor.getString(cursor.getColumnIndex("MeettingContent"));
            String room_name = cursor.getString(cursor.getColumnIndex("room_name"));
            String dInfo = cursor.getString(cursor.getColumnIndex("DepartMentInfo"));
            String date = cursor.getString(cursor.getColumnIndex("chooseDate"));
            MeettingInfo meettingInfo = new MeettingInfo();
            meettingInfo.setStart_time(start_time);
            meettingInfo.setEnd_time(end_time);
            meettingInfo.setChooseDate(date);
            meettingInfo.setRoomName(room_name);
            meettingInfo.setContent(content);
            meettingInfo.setDepartmentInfo(dInfo);
            LogT.d(" queryAllDayofMeetting  " + meettingInfo);
            String time = date + end_time;
            Date time_date = dateFormat.parse(time, new ParsePosition(0));
            if (!time_date.before(this.date)) {
                LogT.d("已经完结的会议时间将不再添加");
                meettingContentList.add(meettingInfo);
            }
        }
        // 关闭光标
        cursor.close();
        return meettingContentList;
    }

    /**
     * 在CalendarChooseActivity的onresume函数中使用。用于查询当前会议室内的所有会议，包括已过期会议
     *
     * @param roomName
     * @return List<MeettingContent>
     */
    public List<MeettingInfo> queryAllDayToSignScheme(String roomName) {

        List<MeettingInfo> meettingContentList = new ArrayList<>();
        Cursor cursor = getWritableDatabase().query(TABLE_NAME, null,
                "room_name = ?", new String[]{roomName},
                null, null, null, null);
        LogT.d(" cursor count " + cursor.getCount());
        while (cursor.moveToNext()) {
            // 先获取 name 的索引值，然后再通过索引获取字段值
            String start_time = cursor.getString(cursor.getColumnIndex("Start_Time"));
            String end_time = cursor.getString(cursor.getColumnIndex("End_Time"));
            String content = cursor.getString(cursor.getColumnIndex("MeettingContent"));
            String room_name = cursor.getString(cursor.getColumnIndex("room_name"));
            String dInfo = cursor.getString(cursor.getColumnIndex("DepartMentInfo"));
            String date = cursor.getString(cursor.getColumnIndex("chooseDate"));
            MeettingInfo meettingInfo = new MeettingInfo();
            meettingInfo.setStart_time(start_time);
            meettingInfo.setEnd_time(end_time);
            meettingInfo.setChooseDate(date);
            meettingInfo.setRoomName(room_name);
            meettingInfo.setContent(content);
            meettingInfo.setDepartmentInfo(dInfo);
            LogT.d(" queryAllDayofMeetting  " + meettingInfo);
            meettingContentList.add(meettingInfo);
        }
        cursor.close();
        return meettingContentList;
    }

    public void deleteMeetting(MeettingInfo content) {
        LogT.d("要删除的会议内容是:" + content.toString());
        getWritableDatabase().delete(TABLE_NAME, "Start_Time = ? " +
                "and End_Time = ?" +
                "and MeettingContent = ?" +
                "and room_name = ?" +
                "and DepartMentInfo = ?" +
                "and chooseDate = ?", new String[]{content.getStart_time(), content.getEnd_time(),
                content.getContent(), content.getRoomName(), content.getDepartmentInfo(), content.getChooseDate()});
    }

    public void insertRoom(MeettingRomItem meettingRomItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("room_name", meettingRomItem.getName());
        contentValues.put("room_status", meettingRomItem.getStatus());
        contentValues.put("room_event", meettingRomItem.getTodo_Count());
        getWritableDatabase().insert(TABLE_NAME_ROOM, null, contentValues);
    }

    public void insertDepartment(DepartmentInfo departmentInfo) {
        LogT.d("departmentInfo is " + departmentInfo);
        ContentValues contentValues = new ContentValues();
        contentValues.put("depart_name", departmentInfo.getName());
        contentValues.put("Start_Time", departmentInfo.getStart_time());
        contentValues.put("End_Time", departmentInfo.getEnd_time());
        contentValues.put("depart_room_name", departmentInfo.getRoom_name());
        contentValues.put("inMeetting", departmentInfo.isInTheMeetting() ? 1 : 0);
        getWritableDatabase().insert(TABLE_NAME_DEPARTMENT, null, contentValues);
    }

    public void updateRoom(MeettingRomItem data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("room_status", data.getStatus());
        contentValues.put("room_event", data.getTodo_Count());
        getWritableDatabase().update(TABLE_NAME_ROOM, contentValues, "room_name = ?", new String[]{data.getName()});
    }

    public void deleteRoom(String roomname) {
        getWritableDatabase().delete(TABLE_NAME_ROOM, "room_name = ?", new String[]{roomname});
        getWritableDatabase().delete(TABLE_NAME, "room_name = ?", new String[]{roomname});
    }

    public ArrayList<MeettingRomItem> getList() {
        ArrayList<MeettingRomItem> list = new ArrayList<>();
        Cursor cursor = getWritableDatabase().query(TABLE_NAME_ROOM, null,
                null, null,
                null, null, null, null);
        if (cursor.getCount() > 0) {
            LogT.d(" cursor.getCount() > 0 ");
            //移动到首位
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                MeettingRomItem meettingRomItem = new MeettingRomItem();
                meettingRomItem.setName(cursor.getString(cursor.getColumnIndex("room_name")));
                meettingRomItem.setStatus(cursor.getInt(cursor.getColumnIndex("room_status")));
                meettingRomItem.setTodo_Count(cursor.getInt(cursor.getColumnIndex("room_event")));
                list.add(meettingRomItem);
                //移动到下一位
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public ArrayList<DepartmentInfo> getDepartList() {
        ArrayList<DepartmentInfo> list = new ArrayList<>();
        Cursor cursor = getWritableDatabase().query(TABLE_NAME_DEPARTMENT, null,
                null, null,
                null, null, null, null);
        if (cursor.getCount() > 0) {
            LogT.d(" cursor.getCount() > 0 " + cursor.getCount());
            //移动到首位
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                DepartmentInfo departmentInfo = new DepartmentInfo();
                departmentInfo.setName(cursor.getString(cursor.getColumnIndex("depart_name")));
                departmentInfo.setStart_time(cursor.getString(cursor.getColumnIndex("Start_Time")));
                departmentInfo.setEnd_time(cursor.getString(cursor.getColumnIndex("End_Time")));
                departmentInfo.setInTheMeetting(cursor.getInt(cursor.getColumnIndex("depart_name")) == 1);
                departmentInfo.setRoom_name(cursor.getString(cursor.getColumnIndex("depart_room_name")));
                list.add(departmentInfo);
                //移动到下一位
                cursor.moveToNext();
            }
        }
        cursor.close();
        LogT.d(" list size is " + list.size());
        return list;
    }

    private String appendZero(int time) {
        if (time < 10) {
            return "0" + String.valueOf(time);
        } else {
            return String.valueOf(time);
        }
    }
}
