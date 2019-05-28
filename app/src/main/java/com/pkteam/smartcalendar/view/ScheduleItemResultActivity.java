package com.pkteam.smartcalendar.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;

import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.adapter.RecyclerMainAdapter;
import com.pkteam.smartcalendar.databinding.ActivityScheduleItemResultBinding;
import com.pkteam.smartcalendar.model.MyData;
import com.singh.daman.gentletoast.GentleToast;

import java.util.ArrayList;

public class ScheduleItemResultActivity extends AppCompatActivity {

    ActivityScheduleItemResultBinding binding;
    ArrayList<MyData> mDataList = new ArrayList<>();
    ArrayList<MyData> scheduledStaticList = new ArrayList<>();
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_item_result);
        binding.setScheduling(this);
        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);

        Intent getIntent = getIntent();
        scheduledStaticList = (ArrayList<MyData>) getIntent.getSerializableExtra("scheduled");

        int sId = -1;
        for(MyData scheduled : scheduledStaticList){
            scheduled.setMode(112);
            if(sId != scheduled.mScheduleId){
                sId = scheduled.mScheduleId;
                mDataList.add(new MyData(scheduled.mTitle, 0));
                mDataList.add(scheduled);
            }else{
                mDataList.add(scheduled);
            }
        }

        binding.recyclerScheduled.setHasFixedSize(true);
        binding.recyclerScheduled.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerScheduled.scrollToPosition(0);
        RecyclerMainAdapter adapter = new RecyclerMainAdapter(getApplicationContext(), mDataList);
        binding.recyclerScheduled.setAdapter(adapter);
        binding.recyclerScheduled.setItemAnimator(new DefaultItemAnimator());

    }

    public void completeListener(View view){
        for (MyData scheduledData : scheduledStaticList){
            dbHelper.todoDataInsert(scheduledData);
            // id가 sid인 dynamic 스케줄의 sid를 1로 바꿔준다 (스케줄링 됨을 의미)
            dbHelper.editSchedulingId(scheduledData.mScheduleId, 1);
        }

        GentleToast.with(getApplicationContext()).longToast("생성 되었습니다.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
        finish();
    }

    public void finishView(View view){
        GentleToast.with(getApplicationContext()).longToast("다시 스케줄링 해보세요!\n혹은 스케줄링 모드를 바꿔보세요!").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
        finish();
    }
}
