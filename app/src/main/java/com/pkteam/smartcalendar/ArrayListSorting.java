package com.pkteam.smartcalendar;

import android.util.Log;

import com.pkteam.smartcalendar.model.MyData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by paeng on 2018. 7. 29..
 */

public class ArrayListSorting {

    public ArrayListSorting(){

    }

    public ArrayList<MyData> sortingForStaticForToday(ArrayList<MyData> inputAl){
        ArrayList<MyData> outputAl = new ArrayList<>();


        // 현재 날짜인 Static Item 만 따로 뽑는다.
        for (int i=0; i<inputAl.size();i++){

            int startDate = Integer.valueOf(inputAl.get(i).getmTime().split("\\.")[0].substring(0, 8));
            int endDate = Integer.valueOf(inputAl.get(i).getmTime().split("\\.")[1].substring(0, 8));

            if(startDate <= Integer.valueOf(getCurrentDate()) && endDate >= Integer.valueOf(getCurrentDate())){
                outputAl.add(inputAl.get(i));
            }
        }

        // (BubbleSort) 시간별로 정렬한다. (시작 시간 순서로)
        String element1, element2;
        for (int i=0; i<outputAl.size();i++){
            for (int j=0; j<outputAl.size() - 1; j++){
                element1 = outputAl.get(j).getmTime().split("\\.")[0];
                element2 = outputAl.get(j+1).getmTime().split("\\.")[0];


                if (Long.parseLong(element1)>Long.parseLong(element2)){
                    MyData buffer1, buffer2;
                    buffer1 = outputAl.get(j);
                    buffer2 = outputAl.get(j+1);
                    outputAl.set(j,buffer2);
                    outputAl.set(j+1, buffer1);
                }

            }
        }

        return outputAl;
    }

    public ArrayList<MyData> sortingForDynamicForToday(ArrayList<MyData> inputAl){
        ArrayList<MyData> outputAl = new ArrayList<>();


        return outputAl;
    }

    public ArrayList<MyData> sortingForDynamicFromToday(ArrayList<MyData> inputAl){
        ArrayList<MyData> outputAl = new ArrayList<>();


        // 오늘 날짜 이후인 Dynamic Item 만 따로 뽑는다.
        for (int i=0; i<inputAl.size();i++){

            int deadlineDate = Integer.valueOf(inputAl.get(i).getmTime().split("\\.")[2].substring(0, 8));

            if(deadlineDate >= Integer.valueOf(getCurrentDate())){
                outputAl.add(inputAl.get(i));
            }
        }

        // (BubbleSort) 시간별로 정렬한다. (시작 시간 순서로)
        String element1, element2;
        for (int i=0; i<outputAl.size();i++){
            for (int j=0; j<outputAl.size() - 1; j++){
                element1 = outputAl.get(j).getmTime().split("\\.")[2];
                element2 = outputAl.get(j+1).getmTime().split("\\.")[2];


                if (Long.parseLong(element1)>Long.parseLong(element2)){
                    MyData buffer1, buffer2;
                    buffer1 = outputAl.get(j);
                    buffer2 = outputAl.get(j+1);
                    outputAl.set(j,buffer2);
                    outputAl.set(j+1, buffer1);
                }

            }
        }

        return outputAl;
    }

    private String getCurrentDate(){
        // get Current Date and Time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String[] mDate = sdf.format(date).split("/");
        return mDate[0]+mDate[1]+mDate[2];
    }
}
