package com.pkteam.smartcalendar;

/**
 * Created by paeng on 2018. 7. 5..
 */

public class MyData {
    public int mId;
    public String mTitle;
    public String mLocation;
    public String mMemo;
    public int mCategory;
    public String mTime;
    public boolean mIsFixed;
    public boolean mIsAllday;
    public String mIsNeedTime;

    public MyData(int category, String title, String time){
        this.mCategory = category;
        this.mTitle = title;
        this.mTime = time;
    }
}
