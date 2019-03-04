package com.pkteam.smartcalendar.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;

import com.pkteam.smartcalendar.ArrayListSorting;
import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.RecyclerViewAdapter;
import com.pkteam.smartcalendar.databinding.ActivityScheduleItemBinding;
import com.pkteam.smartcalendar.model.MyData;

import java.util.ArrayList;

public class ScheduleItemActivity extends AppCompatActivity {
    private ArrayListSorting arrayListSorting = new ArrayListSorting();
    ActivityScheduleItemBinding binding;
    private ArrayList<MyData> dynamicData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_item);
        binding.setScheduling(this);
        resetDataSet();
        initRecyclerView(binding.getRoot());
    }

    private void resetDataSet(){
        DBHelper dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);
        dynamicData = new ArrayList<>();
        dynamicData = dbHelper.getTodoDynamicData();
    }

    private void initRecyclerView(View mView){

        // dynamic
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.scrollToPosition(0);
        binding.recyclerView.setAdapter(new RecyclerViewAdapter(mView.getContext(), arrayListSorting.sortingForDynamicFromToday(dynamicData),3));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void finishView(View view){
        finish();
    }

    public void onClickFooter(View view){
        finish();
    }
}