package com.pkteam.smartcalendar.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_datacheck);
        initRecyclerView();

        Log.d("DataCheckPaeng", "=========================================================================");
        Log.d("DataCheckPaeng", "Size:" + String.valueOf(allData.size()));
        Log.d("DataCheckPaeng", "Id/Title/Location/isDynamic/isAllday/Time/Category/Memo/NeedTime/RepeatId");
        Log.d("DataCheckPaeng", "-------------------------------------------------------------------------");
        for (int i=0; i<allData.size(); i++){
            Log.d("DataCheckPaeng", String.valueOf(allData.get(i).mId)+"/"+String.valueOf(allData.get(i).mTitle)+"/"+String.valueOf(allData.get(i).mLocation)+"/"+String.valueOf(allData.get(i).mIsDynamic)+"/"+String.valueOf(allData.get(i).mIsAllday)+"/"+String.valueOf(allData.get(i).mTime)+"/"+String.valueOf(allData.get(i).mCategory)+"/"+String.valueOf(allData.get(i).mMemo)+"/"+String.valueOf(allData.get(i).mNeedTime)+"/"+String.valueOf(allData.get(i).mRepeatId+"/"));
        }


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
        mAdapter = new RecyclerViewAdapter(getApplicationContext(), allData,1);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }
}
