package com.pkteam.smartcalendar;

/*
 * Created by paeng on 2018. 7. 12.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pkteam.smartcalendar.model.MyData;

import java.util.ArrayList;


// 0.id(Int)    1.title(String)  2.loc(String)   3.isStatic(boolean)  4.isAllday(boolean)
// 5.time(String)    6.category(Int)     7.Memo(String)  8.NeedTime(int)    9.repeatId(Int)    10.scheduleId(Int)

public class DBHelper extends SQLiteOpenHelper {
    private static final String basicCg1 = "기본";
    private static final String basicCg2 = "약속";
    private static final String basicCg3 = "공부";
    private static final String basicCg4 = "활동";
    private static final String basicCg5 = "기타";

    private static final String basicSleepTimeStart = "0000";
    private static final String basicSleepTimeEnd = "0600";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TODOLIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, location TEXT," +
                " isDynamic BOOLEAN, isAllday BOOLEAN, time TEXT, category TEXT, memo TEXT, needTime TEXT, repeatId INTEGER, scheduleId INTEGER);");

        // 카테고리 내부 Database
        db.execSQL("CREATE TABLE CATEGORY (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
        db.execSQL("INSERT INTO CATEGORY VALUES(null, '" + basicCg1 + "');");
        db.execSQL("INSERT INTO CATEGORY VALUES(null, '" + basicCg2 + "');");
        db.execSQL("INSERT INTO CATEGORY VALUES(null, '" + basicCg3 + "');");
        db.execSQL("INSERT INTO CATEGORY VALUES(null, '" + basicCg4 + "');");
        db.execSQL("INSERT INTO CATEGORY VALUES(null, '" + basicCg5 + "');");

        // 반복 id 설정 내부 Database
        db.execSQL("CREATE TABLE USERINFO (_id INTEGER PRIMARY KEY AUTOINCREMENT, repeatId INTEGER);");
        db.execSQL("INSERT INTO USERINFO VALUES(null, '" + 0 + "');");

        // 수면시간 내부 Database
        db.execSQL("CREATE TABLE SLEEPTIME (_id INTEGER PRIMARY KEY AUTOINCREMENT, time TEXT);");
        db.execSQL("INSERT INTO SLEEPTIME VALUES(null, '" +basicSleepTimeStart+ "');");
        db.execSQL("INSERT INTO SLEEPTIME VALUES(null, '" +basicSleepTimeEnd+ "');");

        // 스케줄링 모드 Database
        db.execSQL("CREATE TABLE SCMODE (_id INTEGER PRIMARY KEY AUTOINCREMENT, scheduleMode INTEGER);");
        db.execSQL("INSERT INTO SCMODE VALUES(null, '" + 1 +"');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    // --- to do Data Database ---
    public void todoDataInsert(String title, String location, boolean isDynamic, boolean isAllday, String time, int category, String memo, int needTime, int repeatId, int scheduleId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TODOLIST VALUES(null, '" + title + "', '" + location + "', '" + isDynamic + "' , '"+isAllday+"' , '"+time+"', '"+category+"', '"+memo+"','"+needTime+"', '"+repeatId+"', '"+scheduleId+"');");
        db.close();
    }
    public void todoDataInsert(MyData data){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TODOLIST VALUES(null, '" + data.mTitle + "', '" + data.mLocation + "', '" + data.mIsDynamic + "' , '"+data.mIsAllday+"' , '"+data.mTime+"', '"+data.mCategory+"', '"+data.mMemo+"','"+data.mNeedTime+"', '"+data.mRepeatId+"', '"+data.mScheduleId+"');");
        db.close();
    }
    public void todoDataUpdate(int id, String title, String location, boolean isDynamic, boolean isAllday, String time, int category, String memo, int needTime, int repeatId, int scheduleId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TODOLIST SET title='" + title + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET location='" + location + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET isDynamic='" + isDynamic + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET isAllday='" + isAllday + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET time='" + time + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET category='" + category + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET memo='" + memo + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET needTime='" + needTime + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET repeatId='" + repeatId + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET scheduleId='" + scheduleId + "' WHERE _id='" + id + "';");
        db.close();
    }
    public ArrayList<MyData> getTodoAllData(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<MyData> alMyData = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM TODOLIST", null);

        while (cursor.moveToNext()){
            MyData dataElement = new MyData(
                    Integer.valueOf(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    Boolean.valueOf(cursor.getString(3)),
                    Boolean.valueOf(cursor.getString(4)),
                    cursor.getString(5),
                    Integer.valueOf(cursor.getString(6)),
                    cursor.getString(7),
                    Integer.valueOf(cursor.getString(8)),
                    Integer.valueOf(cursor.getString(9)),
                    Integer.valueOf(cursor.getString(10))

            );
            alMyData.add(dataElement);

        }

        return alMyData;
    }
    public ArrayList<MyData> getTodoStaticData(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<MyData> alMyData = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM TODOLIST WHERE (isDynamic = 'false');", null);

        while (cursor.moveToNext()){
            MyData dataElement = new MyData(
                    Integer.valueOf(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    Boolean.valueOf(cursor.getString(3)),
                    Boolean.valueOf(cursor.getString(4)),
                    cursor.getString(5),
                    Integer.valueOf(cursor.getString(6)),
                    cursor.getString(7),
                    Integer.valueOf(cursor.getString(8)),
                    Integer.valueOf(cursor.getString(9)),
                    Integer.valueOf(cursor.getString(10))
            );
            alMyData.add(dataElement);

        }

        return alMyData;
    }
    public ArrayList<MyData> getTodoDynamicData(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<MyData> alMyData = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM TODOLIST WHERE (isDynamic = 'true');", null);

        while (cursor.moveToNext()){
            MyData dataElement = new MyData(
                    Integer.valueOf(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    Boolean.valueOf(cursor.getString(3)),
                    Boolean.valueOf(cursor.getString(4)),
                    cursor.getString(5),
                    Integer.valueOf(cursor.getString(6)),
                    cursor.getString(7),
                    Integer.valueOf(cursor.getString(8)),
                    Integer.valueOf(cursor.getString(9)),
                    Integer.valueOf(cursor.getString(10))
            );
            alMyData.add(dataElement);

        }

        return alMyData;
    }
    public MyData getDataById(int id){
        SQLiteDatabase db = getReadableDatabase();
        MyData dataElement = null;
        Cursor cursor = db.rawQuery("SELECT * FROM TODOLIST WHERE _id = '" + id + "';", null);
        while (cursor.moveToNext()){
            dataElement = new MyData(
                    Integer.valueOf(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    Boolean.valueOf(cursor.getString(3)),
                    Boolean.valueOf(cursor.getString(4)),
                    cursor.getString(5),
                    Integer.valueOf(cursor.getString(6)),
                    cursor.getString(7),
                    Integer.valueOf(cursor.getString(8)),
                    Integer.valueOf(cursor.getString(9)),
                    Integer.valueOf(cursor.getString(10))
            );
        }
        return dataElement;

    }
    public void deleteTodoDataAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TODOLIST;");
        db.close();
    }
    public void deleteTodoDataById(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TODOLIST WHERE _id = '" + id + "';");
        db.close();
    }
    public void deleteTodoDataByRepeatId(int id, int repeatId, String date){
        // 201905301030.201905301130.000000000000
        SQLiteDatabase db=  getWritableDatabase();
        db.execSQL("DELETE FROM TODOLIST WHERE repeatId = '" + repeatId + "';");
        db.close();

    }


    // --- Repeat ID Database ---
    public int getCurrentRepeatId(){
        SQLiteDatabase dbr = getReadableDatabase();
        Cursor cursor = dbr.rawQuery("SELECT * FROM USERINFO;", null);
        String result = "";
        while (cursor.moveToNext()){
            result = cursor.getString(1);
        }
        return Integer.parseInt(result);
    }

    public void updateRepeatId(){
        int resultInt = getCurrentRepeatId();
        resultInt += 1;
        SQLiteDatabase dbw = getWritableDatabase();
        dbw.execSQL("UPDATE USERINFO SET repeatId='" + resultInt + "' WHERE _id='" + 1 + "';");
    }
    public void initializeRepeatId(){
        SQLiteDatabase dbw = getWritableDatabase();
        dbw.execSQL("UPDATE USERINFO SET repeatId='" + 0 + "' WHERE _id='" + 1 + "';");
    }

    // --- Category Database ---
    public ArrayList<String> getAllCategory(){
        ArrayList<String> categoryList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CATEGORY;", null);
        while (cursor.moveToNext()){
            categoryList.add(cursor.getString(1));
        }
        return categoryList;
    }
    public void categoryUpdate(String cg1, String cg2, String cg3, String cg4, String cg5){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE CATEGORY SET name='"+cg1+"' WHERE _id='" + 1 + "';");
        db.execSQL("UPDATE CATEGORY SET name='"+cg2+"' WHERE _id='" + 2 + "';");
        db.execSQL("UPDATE CATEGORY SET name='"+cg3+"' WHERE _id='" + 3 + "';");
        db.execSQL("UPDATE CATEGORY SET name='"+cg4+"' WHERE _id='" + 4 + "';");
        db.execSQL("UPDATE CATEGORY SET name='"+cg5+"' WHERE _id='" + 5 + "';");
        db.close();
    }

    // --- SleepTime Database ---
    public ArrayList<String> getAllSleepTime(){
        ArrayList<String> sleepList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SLEEPTIME;", null);
        while (cursor.moveToNext()){
            sleepList.add(cursor.getString(1));
        }
        return sleepList;
    }

    public void sleepTimeUpdate(String startTime, String endTime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE SLEEPTIME SET time='"+startTime+"' WHERE _id='" + 1 + "';");
        db.execSQL("UPDATE SLEEPTIME SET time='"+endTime+"' WHERE _id='" + 2 + "';");
        db.close();
    }

    // --- Scheduling MODE Database ---
    public int getSchedulingMode(){
        SQLiteDatabase dbr = getReadableDatabase();
        Cursor cursor = dbr.rawQuery("SELECT * FROM SCMODE;", null);
        String result = "";
        while (cursor.moveToNext()){
            result = cursor.getString(1);
        }
        return Integer.parseInt(result);
    }

    public void updateSchedulingMode(int mode){
        SQLiteDatabase dbw = getWritableDatabase();
        dbw.execSQL("UPDATE SCMODE SET scheduleMode='" + mode +"' WHERE _id='" + 1 +"';");
    }

}
