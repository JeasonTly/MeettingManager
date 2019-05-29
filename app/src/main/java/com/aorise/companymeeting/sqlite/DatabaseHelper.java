package com.aorise.companymeeting.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aorise.companymeeting.base.LogT;
import com.aorise.companymeeting.base.MeettingContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/28.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "meettingcontent.db";
    public static final String TABLE_NAME = "MeettingTime";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context, String name, int version, SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String sql = "create table user(name varchar(20))";
        String sql = "create table if not exists " + TABLE_NAME + " (Id integer primary key autoincrement, StartYear text , StartMonth text, StartDay text, " +
                "StartHour text,StartMinutes text,EndHour text,EndMinutes text ,MeettingContent text)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public void insertMeetting(MeettingContent content) {
        Log.d("DatabaseHelper", " insertMeetting content is " + content.toString());
        ContentValues contentValues = new ContentValues();
        //contentValues.put("Id", content.getId());
        contentValues.put("StartYear", content.getStart_year());
        contentValues.put("StartMonth", content.getStart_month());
        contentValues.put("StartDay", content.getStart_day());
        contentValues.put("StartHour", content.getStart_hour());
        contentValues.put("StartMinutes", content.getStart_minutes());
        contentValues.put("EndHour", content.getEnd_hour());
        contentValues.put("EndMinutes", content.getEnd_minutes());
        contentValues.put("MeettingContent", content.getContent());

        getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }

    public List<MeettingContent> queryMeetting(String start_year, String start_month, String start_day) {
        LogT.d(" start_year " + start_year + " start_month " + start_month + " start_day " + start_day);
        List<MeettingContent> meettingContentList = new ArrayList<>();
        Cursor cursor = getWritableDatabase().query(TABLE_NAME, null,
                "StartYear = ? and StartMonth = ? and StartDay = ?", new String[]{start_year, start_month, start_day},
                null, null, null, null);

        // 不断移动光标获取值
        while (cursor.moveToNext()) {
            // 直接通过索引获取字段值
            long id = cursor.getLong(0);

            // 先获取 name 的索引值，然后再通过索引获取字段值
            String startYear = cursor.getString(cursor.getColumnIndex("StartYear"));
            String startMonth = cursor.getString(cursor.getColumnIndex("StartMonth"));
            String startDay = cursor.getString(cursor.getColumnIndex("StartDay"));
            String startHour = cursor.getString(cursor.getColumnIndex("StartHour"));
            String startMinutes = cursor.getString(cursor.getColumnIndex("StartMinutes"));
            String endHour = cursor.getString(cursor.getColumnIndex("EndHour"));
            String endMinutes = cursor.getString(cursor.getColumnIndex("EndMinutes"));
            String meettingContent_value = cursor.getString(cursor.getColumnIndex("MeettingContent"));
            MeettingContent meettingContent = new MeettingContent(startYear, startMonth, startDay, startHour, startMinutes, endHour, endMinutes);
            meettingContent.setId(id);
            meettingContent.setContent(meettingContent_value);
            Log.d("DatabaseHelper", " meetting content is " + meettingContent);
            meettingContentList.add(meettingContent);
        }
        // 关闭光标
        cursor.close();
        return meettingContentList;
    }

    public void deleteMeetting(MeettingContent content) {

        getWritableDatabase().delete(TABLE_NAME, "StartYear = ? and " +
                "StartMonth = ?" +
                "StartDay = ?" +
                "StartHour = ?" +
                "StartMinutes = ?" +
                "EndHour = ?" +
                "EndMinutes = ?", new String[]{content.getStart_year(), content.getStart_month(),
                content.getStart_day(), content.getStart_hour(), content.getStart_minutes(), content.getEnd_hour(), content.getEnd_minutes()});
    }
}
