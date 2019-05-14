package com.pkteam.smartcalendar.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.adapter.RecyclerMainAdapter;
import com.pkteam.smartcalendar.databinding.ActivityScheduleItemResultBinding;
import com.pkteam.smartcalendar.model.MyData;
import com.singh.daman.gentletoast.GentleToast;

import java.util.ArrayList;

public class ScheduleItemResultActivity extends AppCompatActivity {

    ActivityScheduleItemResultBinding binding;
    ArrayList<MyData> mDataList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_item_result);
        binding.setScheduling(this);

        Intent getIntent = getIntent();
        ArrayList<MyData> scheduledStaticList = (ArrayList<MyData>) getIntent.getSerializableExtra("scheduled");

        int sId = -1;
        for(MyData scheduled : scheduledStaticList){
            scheduled.setMode(111);
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
        GentleToast.with(getApplicationContext()).longToast("생성 되었습니다.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();


    }

    public void finishView(View view){
        finish();
    }
}
