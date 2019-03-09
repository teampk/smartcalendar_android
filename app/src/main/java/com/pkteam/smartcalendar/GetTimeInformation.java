package com.pkteam.smartcalendar;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetTimeInformation {
    public GetTimeInformation(){

    }

    public String getCurrentDate(){
        // get Current Date and Time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String[] mDate = sdf.format(date).split("/");
        return mDate[0]+mDate[1]+mDate[2];
    }

    public String getCurrentDate(long time){
        // yyyy-MM-dd hh:mm:ss
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");

        return simpleDate.format(new Date(time));

    }

    public int getDdayInt(String input){
        Calendar tday = Calendar.getInstance();
        Calendar dday = Calendar.getInstance();
        //20180725

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String[] mDate = sdf.format(date).split("/");
        tday.set(Integer.valueOf(mDate[0]),
                Integer.valueOf(mDate[1]),
                Integer.valueOf(mDate[2]));

        dday.set(Integer.valueOf(input.substring(0,4)),
                Integer.valueOf(input.substring(4,6)),
                Integer.valueOf(input.substring(6,8)));

        return (int)((tday.getTimeInMillis()/86400000) - (dday.getTimeInMillis()/86400000));
    }

    public int getdTimeInt(String input){
        Calendar tday = Calendar.getInstance();
        Calendar dday = Calendar.getInstance();
        //20180725

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/kk/mm");
        String[] mDate = sdf.format(date).split("/");
        tday.set(Integer.valueOf(mDate[0]),
                Integer.valueOf(mDate[1]),
                Integer.valueOf(mDate[2]),
                Integer.valueOf(mDate[3]),
                Integer.valueOf(mDate[4]));

        dday.set(Integer.valueOf(input.substring(0,4)),
                Integer.valueOf(input.substring(4,6)),
                Integer.valueOf(input.substring(6,8)),
                Integer.valueOf(input.substring(8,10)),
                Integer.valueOf(input.substring(10,12)));

        return (int)((tday.getTimeInMillis()/60000) - (dday.getTimeInMillis()/60000));
    }

    public String getDday(String input){
        Calendar tday = Calendar.getInstance();
        Calendar dday = Calendar.getInstance();
        //20180725

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String[] mDate = sdf.format(date).split("/");
        tday.set(Integer.valueOf(mDate[0]),
                Integer.valueOf(mDate[1]),
                Integer.valueOf(mDate[2]));

        dday.set(Integer.valueOf(input.substring(0,4)),
                Integer.valueOf(input.substring(4,6)),
                Integer.valueOf(input.substring(6,8)));

        int count = (int)((tday.getTimeInMillis()/86400000) - (dday.getTimeInMillis()/86400000));
        String output;
        if (count == 0){
            output = "D-0";
        }else if (count > 0){
            output = "D+"+String.valueOf(count);
        }else {
            output = "D"+String.valueOf(count);
        }
        return output;
    }


}
