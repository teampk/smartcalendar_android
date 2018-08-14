package com.pkteam.smartcalendar.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.RecyclerViewAdapter;
import com.pkteam.smartcalendar.model.MyData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * Created by paeng on 2018. 7. 29..
 */

public class DataCheckActivity extends AppCompatActivity{

    ArrayList<MyData> allData;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacheck);
        initRecyclerView();


    }
    @Override
    protected void onResume(){
        super.onResume();
        initRecyclerView();
    }


    private void initRecyclerView(){
        DBHelper dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);
        allData = new ArrayList<>();
        allData = dbHelper.getTodoAllData();
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new RecyclerViewAdapter(getApplicationContext(), allData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }
}
