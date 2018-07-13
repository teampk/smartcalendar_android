package com.pkteam.smartcalendar;

/*
 * Created by paeng on 2018. 7. 12.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pkteam.smartcalendar.model.MyData;

import java.util.ArrayList;


// 0.id(Int)    1.title(String)  2.loc(String)   3.isDynamic(boolean)  4.isAllday(boolean)
// 5.time(String)   6.repeatId(Int)     7.category(Int)     8.Memo(String)  9.NeedTime(int)

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TODOLIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, location TEXT," +
                " isDynamic BOOLEAN, isAllday BOOLEAN, time TEXT, repeatId INTEGER, category TEXT, todoMemo TEXT, todoNeedtime TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
    public void todoDataInsert(String title, String location, boolean isDynamic, boolean isAllday, String time, int repeatId, int category, String memo, int needtime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TODOLIST VALUES(null, '" + title + "', '" + location + "', '" + isDynamic + "' , '"+isAllday+"' , '"+time+"', '"+repeatId+"', '"+category+"', '"+memo+"','"+needtime+"');");
        db.close();
    }
    public ArrayList<MyData> getTodoData(){
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
}
