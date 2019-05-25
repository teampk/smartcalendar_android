package com.pkteam.smartcalendar.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.pkteam.smartcalendar.ArrayListSorting;
import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.adapter.RecyclerMainAdapter;
import com.pkteam.smartcalendar.databinding.FragmentCalendarBinding;
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
    private ArrayListSorting arrayListSorting = new ArrayListSorting();
    private ColorCategory colorCategory = new ColorCategory();
    private DBHelper dbHelper;

    FragmentCalendarBinding binding;

    ArrayList<MyData> mDataList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        final View mView = binding.getRoot();
        dbHelper = new DBHelper(getContext(), "SmartCal.db", null, 1);
        initDataset();
        bindingView(mView);
        setCalendarCircle();
        initSpeedDial(savedInstanceState == null, mView);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initDataset();
        binding.compactcalendarView.setCurrentDate(new Date(System.currentTimeMillis()));
        initRecyclerView(binding.getRoot(), System.currentTimeMillis());
        setCalendarCircle();
    }

    private void bindingView(final View mView){
        binding.compactcalendarView.setFirstDayOfWeek(Calendar.SUNDAY);

        // yyyy-MM-dd hh:mm:ss
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy년 M월");
        String getMonth = simpleDate.format(new Date(System.currentTimeMillis()));
        binding.tvMonth.setText(getMonth);

        binding.compactcalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = binding.compactcalendarView.getEvents(dateClicked);
                initRecyclerView(mView, dateClicked.getTime());
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                int month = firstDayOfNewMonth.getMonth()+1;
                int year = firstDayOfNewMonth.getYear()+1900;
                binding.tvMonth.setText(year+"년 "+month+"월");
                initRecyclerView(mView, firstDayOfNewMonth.getTime());

            }
        });
        initRecyclerView(mView, System.currentTimeMillis());
    }

    private void initDataset(){

        staticData = new ArrayList<>();
        dynamicData = new ArrayList<>();
        staticData = dbHelper.getTodoStaticData();
        dynamicData = dbHelper.getTodoDynamicData();

    }

    private void initRecyclerView(View mView, long time){

        binding.recyclerTotal.setHasFixedSize(true);
        binding.recyclerTotal.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerTotal.scrollToPosition(0);

        mDataList.clear();
        mDataList.add(new MyData(getString(R.string.string_add_item_static), 10));

        // no data in static
        if(arrayListSorting.sortingForStaticForCalendar(staticData, time).size() == 0){
            mDataList.add(new MyData(getString(R.string.string_no_data_calendar_static), 13));
        }else{
            // mode 11
            mDataList.addAll(arrayListSorting.sortingForStaticForCalendar(staticData, time));
        }

        mDataList.add(new MyData(getString(R.string.string_add_item_dynamic), 10));

        // no data in dynamic
        if(arrayListSorting.sortingForDynamicForCalendar(dynamicData, time).size() == 0){
            mDataList.add(new MyData(getString(R.string.string_no_data_calendar_dynamic), 13));
        }else{
            // mode 12
            mDataList.addAll(arrayListSorting.sortingForDynamicForCalendar(dynamicData, time));
        }

        mDataList.add(new MyData("", 10));



        RecyclerMainAdapter mainAdapter = new RecyclerMainAdapter(mView.getContext(), mDataList, time);

        binding.recyclerTotal.setAdapter(mainAdapter);
        binding.recyclerTotal.setItemAnimator(new DefaultItemAnimator());

    }

    private void setCalendarCircle(){
        binding.compactcalendarView.removeAllEvents();

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
                binding.compactcalendarView.addEvent(new Event(colors[0], curDate.getTime(), "1"));
                break;
            case 2:
                binding.compactcalendarView.addEvent(new Event(colors[1], curDate.getTime(), "2"));
                break;
            case 3:
                binding.compactcalendarView.addEvent(new Event(colors[2], curDate.getTime(), "3"));
                break;
            case 4:
                binding.compactcalendarView.addEvent(new Event(colors[3], curDate.getTime(), "4"));
                break;
            case 5:
                binding.compactcalendarView.addEvent(new Event(colors[4], curDate.getTime(), "5"));
                break;
            default:
                break;

        }

    }

    private void initSpeedDial(boolean addActionItems, View mView) {

        if (addActionItems) {

            binding.speedDial.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_add_item, R.drawable.ic_add_white_24dp)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getContext().getTheme()))
                    .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000, getContext().getTheme()))
                    .setLabel(getString(R.string.label_add_item))
                    .setLabelColor(Color.BLACK)
                    .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000, getContext().getTheme()))
                    .create());

            binding.speedDial.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_auto_scheduling, R.drawable.ic_dashboard_white_24dp)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getContext().getTheme()))
                    .setLabel(getString(R.string.label_scheduling))
                    .setLabelColor(Color.BLACK)
                    .setLabelBackgroundColor(Color.WHITE)
                    .setTheme(R.style.AppTheme_Purple)
                    .create());
            binding.speedDial.getUseReverseAnimationOnClose();

        }

        //Set main action clicklistener.
        binding.speedDial.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {

                return false; // True to keep the Speed Dial open
            }

            @Override
            public void onToggleChanged(boolean isOpen) {
                // 열리면 true
                // 닫히면 false
            }
        });

        //Set option fabs clicklisteners.
        binding.speedDial.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_add_item:
                        Intent intent = new Intent(getContext(), AddItemActivity.class);
                        intent.putExtra("mode", 1);
                        startActivity(intent);
                        return false; // closes without animation (same as mSpeedDialView.close(false); return false;)
                    case R.id.fab_auto_scheduling:
                        Intent intentScheduling = new Intent(getContext(), ScheduleItemActivity.class);
                        startActivity(intentScheduling);

                        return false;
                    default:
                        break;
                }
                return true; // To keep the Speed Dial open
            }
        });

    }


}
