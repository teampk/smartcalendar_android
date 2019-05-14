package com.pkteam.smartcalendar.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;

import com.pkteam.smartcalendar.ArrayListSorting;
import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.GetTimeInformation;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.adapter.RecyclerMainAdapter;
import com.pkteam.smartcalendar.databinding.ActivityScheduleItemBinding;
import com.pkteam.smartcalendar.model.MyData;

import java.util.ArrayList;

public class ScheduleItemActivity extends AppCompatActivity {
    private ArrayListSorting arrayListSorting = new ArrayListSorting();
    ActivityScheduleItemBinding binding;
    private ArrayList<MyData> dynamicData;
    RecyclerMainAdapter rcAdapter;
    DBHelper dbHelper;
    GetTimeInformation gti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_item);
        binding.setScheduling(this);
        gti = new GetTimeInformation();
        resetDataSet();
        initRecyclerView(binding.getRoot());
    }

    private void resetDataSet(){
        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);
        dynamicData = new ArrayList<>();
        dynamicData = dbHelper.getTodoDynamicData();
    }

    private void initRecyclerView(View mView){

        // dynamic
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.scrollToPosition(0);
        ArrayList<MyData> returnList = new ArrayList<>();
        for (MyData mList : new ArrayList<>(arrayListSorting.sortingForDynamicFromNow(dynamicData))){
            if(mList.mScheduleId == 0){
                mList.setMode(111);
                returnList.add(mList);
            }
        }
        rcAdapter = new RecyclerMainAdapter (mView.getContext(), returnList);

        binding.recyclerView.setAdapter(rcAdapter);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    public void finishView(View view){
        finish();
    }

    public void onClickFooter(View view){

        Intent mIntent = new Intent(getApplicationContext(), ScheduleItemProgressActivity.class);
        mIntent.putExtra("selectedDynamic", rcAdapter.getSelectedId());
        startActivityForResult(mIntent, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case 3000:
                    int id = data.getIntExtra("error", 0);
                    MyData errorData = dbHelper.getDataById(id);
                    String d = errorData.mTime.split("\\.")[2];
                    String deadline = d.substring(0,4)+"년 "+d.substring(4,6)+"월 "+d.substring(6,8)+"일 "+d.substring(8,10)+":"
                            +d.substring(10)+"  (" +gti.getDday(d)+")";

                    AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleItemActivity.this);
                    builder.setTitle("스케줄링 오류")
                            .setMessage("제목 : "+errorData.mTitle+"\n데드라인 : \n"+deadline+"\n\n위 일정의 필요시간이\n비어있는 시간보다 많아\n스케줄링에 실패하였습니다.")
                            .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    break;

                default:
                    break;
            }
        }else if (resultCode == RESULT_CANCELED){
            finish();
        }
    }
}
