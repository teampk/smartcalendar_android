package com.pkteam.smartcalendar.model;

/*
 * Created by paeng on 2018. 7. 5..
 */

// 0.id(Int)    1.title(String)  2.loc(String)   3.isDynamic(boolean)  4.isAllday(boolean)
// 5.time(String)   6.repeatId(Int)     7.category(Int)     8.Memo(String)  9.NeedTime(int)    10.RepeatId(int)

import java.io.Serializable;

public class MyData implements Serializable {
    public int mId;
    public String mTitle;
    public String mLocation;
    public boolean mIsDynamic;
    public boolean mIsAllday;
    public String mTime;
    public int mCategory;
    public String mMemo;
    public int mNeedTime;
    public int mRepeatId;
    public int mScheduleId;

    public boolean selected;
    public int mode;


    public String mHeader;

    public MyData(String text, int mode){
        this.mHeader = text;
        this.mode = mode;
    }


    public MyData(int id, String title, String location, boolean isDynamic, boolean isAllday,
                  String time, int category, String memo, int needTime, int repeatId, int scheduleId){
        this.mId = id;
        this.mTitle = title;
        this.mLocation = location;
        this.mIsDynamic = isDynamic;
        this.mIsAllday = isAllday;
        this.mTime = time;
        this.mCategory = category;
        this.mMemo = memo;
        this.mNeedTime = needTime;
        this.mRepeatId = repeatId;
        this.mScheduleId = scheduleId;

    }
    public void setmId(int id) {
        this.mId = id;
    }
    public int getmId(){
        return mId;
    }
    public void setmTime(String time){
        this.mTime = time;
    }
    public String getmTime(){
        return this.mTime;
    }

    public Boolean isSelected(){
        if(selected){
            return true;
        }else{
            return false;
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getmHeader() {
        return mHeader;
    }

    public void setmHeader(String mHeader) {
        this.mHeader = mHeader;
    }
}
