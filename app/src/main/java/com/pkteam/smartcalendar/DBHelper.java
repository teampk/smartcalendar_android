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


// 0.id(Int)    1.title(String)  2.loc(String)   3.isDynamic(boolean)  4.isAllday(boolean)
// 5.time(String)   6.repeatId(Int)     7.category(Int)     8.Memo(String)  9.NeedTime(int)

public class DBHelper extends SQLiteOpenHelper {
    private static final String basicCg1 = "기본";
    private static final String basicCg2 = "약속";
    private static final String basicCg3 = "공부";
    private static final String basicCg4 = "활동";
    private static final String basicCg5 = "기타";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TODOLIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, location TEXT," +
                " isDynamic BOOLEAN, isAllday BOOLEAN, time TEXT, repeatId INTEGER, category TEXT, memo TEXT, needTime TEXT);");
        db.execSQL("CREATE TABLE CATEGORY (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
        db.execSQL("INSERT INTO CATEGORY VALUES(null, '" + basicCg1 + "');");
        db.execSQL("INSERT INTO CATEGORY VALUES(null, '" + basicCg2 + "');");
        db.execSQL("INSERT INTO CATEGORY VALUES(null, '" + basicCg3 + "');");
        db.execSQL("INSERT INTO CATEGORY VALUES(null, '" + basicCg4 + "');");
        db.execSQL("INSERT INTO CATEGORY VALUES(null, '" + basicCg5 + "');");

        db.execSQL("CREATE TABLE USERINFO (_id INTEGER PRIMARY KEY AUTOINCREMENT, repeatID INTEGER);");
        db.execSQL("INSERT INTO USERINFO VALUES(null, '" + 0 + "');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
    public void todoDataInsert(String title, String location, boolean isDynamic, boolean isAllday, String time, int repeatId, int category, String memo, int needtime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TODOLIST VALUES(null, '" + title + "', '" + location + "', '" + isDynamic + "' , '"+isAllday+"' , '"+time+"', '"+repeatId+"', '"+category+"', '"+memo+"','"+needtime+"');");
        db.close();
    }
    public void todoDataUpdate(int id, String title, String location, boolean isDynamic, boolean isAllday, String time, int repeatId, int category, String memo, int needTime) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TODOLIST SET title='" + title + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET location='" + location + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET isDynamic='" + isDynamic + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET isAllday='" + isAllday + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET time='" + time + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET repeatId='" + repeatId + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET category='" + category + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET memo='" + memo + "' WHERE _id='" + id + "';");
        db.execSQL("UPDATE TODOLIST SET needTime='" + needTime + "' WHERE _id='" + id + "';");
        db.close();
    }
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
                    Integer.valueOf(cursor.getString(7)),
                    cursor.getString(8),
                    Integer.valueOf(cursor.getString(9))
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
                    Integer.valueOf(cursor.getString(7)),
                    cursor.getString(8),
                    Integer.valueOf(cursor.getString(9))
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
                    Integer.valueOf(cursor.getString(7)),
                    cursor.getString(8),
                    Integer.valueOf(cursor.getString(9))
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
                    Integer.valueOf(cursor.getString(7)),
                    cursor.getString(8),
                    Integer.valueOf(cursor.getString(9))
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
        dbw.execSQL("UPDATE USERINFO SET repeatID='" + resultInt + "' WHERE _id='" + 1 + "';");
    }
    public void initializeRepeatId(){
        SQLiteDatabase dbw = getWritableDatabase();
        dbw.execSQL("UPDATE USERINFO SET repeatID='" + 0 + "' WHERE _id='" + 1 + "';");
    }
}
