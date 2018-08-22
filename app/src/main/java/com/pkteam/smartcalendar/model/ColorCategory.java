package com.pkteam.smartcalendar.model;

import android.support.annotation.ColorInt;

/**
 * Created by paeng on 22/08/2018.
 */

public class ColorCategory {
    @ColorInt public static final int CATEGORY1 = 0xFFF9320C;
    @ColorInt public static final int CATEGORY2 = 0xFFF9C00C;
    @ColorInt public static final int CATEGORY3 = 0xFF75D701;
    @ColorInt public static final int CATEGORY4 = 0xFF00B9F1;
    @ColorInt public static final int CATEGORY5 = 0xFF7200DA;
    public ColorCategory(){

    }
    public int getCategory1(){
        return CATEGORY1;
    }
    public int getCategory2(){
        return CATEGORY2;
    }
    public int getCategory3(){
        return CATEGORY3;
    }
    public int getCategory4(){
        return CATEGORY4;
    }
    public int getCategory5(){
        return CATEGORY5;
    }

}
