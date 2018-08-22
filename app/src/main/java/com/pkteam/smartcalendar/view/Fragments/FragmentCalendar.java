package com.pkteam.smartcalendar.view.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.pkteam.smartcalendar.ArrayListSorting;
import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.RecyclerViewAdapter;
import com.pkteam.smartcalendar.model.ColorCategory;
import com.pkteam.smartcalendar.model.MyData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/*
 * Created by paeng on 2018. 7. 4..
 */

public class FragmentCalendar extends Fragment {
    private ArrayList<MyData> staticData, dynamicData;
    private RecyclerView mRecyclerViewStatic, mRecyclerViewDynamic;
    private RecyclerView.LayoutManager mLayoutManagerStatic, mLayoutManagerDynamic;
    private RecyclerViewAdapter mAdapterStatic, mAdapterDynamic;
    private CompactCalendarView compactCalendarView;
    private ArrayListSorting arrayListSorting = new ArrayListSorting();
    private ColorCategory colorCategory = new ColorCategory();
    private TextView tvMonth;
    private DBHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_calendar, container, false);
        dbHelper = new DBHelper(getContext(), "SmartCal.db", null, 1);
        initDataset();
        bindingView(mView);
        setCalendarCircle();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initDataset();
        setCalendarCircle();
    }

    private void bindingView(View mView){
        compactCalendarView = mView.findViewById(R.id.compactcalendar_view);
        tvMonth = mView.findViewById(R.id.tv_month);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        mRecyclerViewStatic = mView.findViewById(R.id.recycler_static);
        mRecyclerViewDynamic = mView.findViewById(R.id.recycler_dynamic);

        // yyyy-MM-dd hh:mm:ss
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy년 M월");
        String getMonth = simpleDate.format(new Date(System.currentTimeMillis()));
        tvMonth.setText(getMonth);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                setRecyclerView(dateClicked.getTime());

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                int month = firstDayOfNewMonth.getMonth()+1;
                int year = firstDayOfNewMonth.getYear()+1900;
                tvMonth.setText(year+"년 "+month+"월");
                setRecyclerView(firstDayOfNewMonth.getTime());

            }
        });

        setRecyclerView(System.currentTimeMillis());

    }

    private void initDataset(){

        staticData = new ArrayList<>();
        dynamicData = new ArrayList<>();
        staticData = dbHelper.getTodoStaticData();
        dynamicData = dbHelper.getTodoDynamicData();

    }

    private void setRecyclerView(long time){

        mLayoutManagerStatic = new LinearLayoutManager(getActivity());
        mRecyclerViewStatic.setHasFixedSize(true);
        mRecyclerViewStatic.setLayoutManager(mLayoutManagerStatic);
        mRecyclerViewStatic.scrollToPosition(0);
        mAdapterStatic = new RecyclerViewAdapter(getContext(), arrayListSorting.sortingForStaticForCalendar(staticData, time));
        mRecyclerViewStatic.setAdapter(mAdapterStatic);
        mRecyclerViewStatic.setItemAnimator(new DefaultItemAnimator());

        mLayoutManagerDynamic = new LinearLayoutManager(getActivity());
        mRecyclerViewDynamic.setHasFixedSize(true);
        mRecyclerViewDynamic.setLayoutManager(mLayoutManagerDynamic);
        mRecyclerViewDynamic.scrollToPosition(0);
        mAdapterDynamic = new RecyclerViewAdapter(getContext(), arrayListSorting.sortingForDynamicForCalendar(dynamicData, time));
        mRecyclerViewDynamic.setAdapter(mAdapterDynamic);
        mRecyclerViewDynamic.setItemAnimator(new DefaultItemAnimator());


    }
    private void setCalendarCircle(){
        compactCalendarView.removeAllEvents();

        for (int i=0; i<staticData.size(); i++) {
            int startDate = Integer.parseInt(staticData.get(i).getmTime().split("\\.")[0].substring(0, 8));
            int endDate = Integer.parseInt(staticData.get(i).getmTime().split("\\.")[1].substring(0, 8));
            int category = staticData.get(i).mCategory;
            for (int j = startDate; j<=endDate; j++){
                makeCircle(j, category);
            }
        }

        for (int i=0; i<dynamicData.size(); i++) {
            int deadline = Integer.parseInt(dynamicData.get(i).getmTime().split("\\.")[2].substring(0, 8));
            int category = dynamicData.get(i).mCategory;

            makeCircle(deadline, category);

        }

    }
    private void makeCircle(int date, int category){
        int colors[] = {colorCategory.getCategory1(), colorCategory.getCategory2(), colorCategory.getCategory3(), colorCategory.getCategory4(), colorCategory.getCategory5()};
        int colorsAndroid[] = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.BLACK};
        //date = 20180324
        String dateString = String.valueOf(date);
        int year = Integer.parseInt(dateString.substring(0,4));
        int month = Integer.parseInt(dateString.substring(4,6));
        int day = Integer.parseInt(dateString.substring(6,8));

        Date curDate = new Date();
        curDate.setYear(year-1900);
        curDate.setMonth(month-1);
        curDate.setDate(day);

        switch (category){
            case 1:
                compactCalendarView.addEvent(new Event(colors[0], curDate.getTime(), "1"));
                break;
            case 2:
                compactCalendarView.addEvent(new Event(colors[1], curDate.getTime(), "2"));
                break;
            case 3:
                compactCalendarView.addEvent(new Event(colors[2], curDate.getTime(), "3"));
                break;
            case 4:
                compactCalendarView.addEvent(new Event(colors[3], curDate.getTime(), "4"));
                break;
            case 5:
                compactCalendarView.addEvent(new Event(colors[4], curDate.getTime(), "5"));
                break;
            default:
                break;

        }

    }


}
