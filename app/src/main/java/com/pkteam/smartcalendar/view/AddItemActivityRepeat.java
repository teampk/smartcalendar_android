package com.pkteam.smartcalendar.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pkteam.smartcalendar.R;

/**
 * Created by paeng on 2018. 7. 11..
 */

public class AddItemActivityRepeat extends AppCompatActivity {
    private LinearLayout llRepeatNothing, llRepeatDay, llRepeatWeek, llRepeatMonth, llRepeatYear;
    private LinearLayout llRepeatDayDetail, llRepeatWeekDetail, llRepeatMonthDetail, llRepeatYearDetail;
    private ImageView ivRepeatCheckNothing, ivRepeatCheckDay, ivRepeatCheckWeek, ivRepeatCheckMonth, ivRepeatCheckYear;
    private int checkedRepeat;
    private LinearLayout llCancel;
    private Button btnSubmit;
    private String repeatMode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_additem_repeat);
        bindingView();

        Intent intent = getIntent();
        this.repeatMode = intent.getStringExtra("repeatMode");
        checkedRepeat = setClickedView(getRepeatInteger(this.repeatMode));

    }

    private int getRepeatInteger(String inputString){
        int mode=0;

        switch (inputString){
            case "안 함":
                mode=1;
                break;
            case "매일":
                mode=2;
                break;
            case "매주":
                mode=3;
                break;
            case "매월":
                mode=4;
                break;
            case "매년":
                mode=5;
                break;
            default:
                Toast.makeText(AddItemActivityRepeat.this, "error occured in repeat", Toast.LENGTH_SHORT).show();
                break;
        }
        return mode;
    }

    private String getRepeatString(int mode){
        String returnString = "";
        switch (mode){
            case 1:
                returnString = "안 함";
                break;
            case 2:
                returnString = "매일";
                break;
            case 3:
                returnString = "매주";
                break;
            case 4:
                returnString = "매월";
                break;
            case 5:
                returnString = "매년";
                break;
            default:
                Toast.makeText(AddItemActivityRepeat.this, "error occured in repeat", Toast.LENGTH_SHORT).show();
                break;
        }
        return returnString;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_repeat_nothing:
                    checkedRepeat = setClickedView(1);
                    break;
                case R.id.ll_repeat_day:
                    checkedRepeat = setClickedView(2);
                    break;
                case R.id.ll_repeat_week:
                    checkedRepeat = setClickedView(3);
                    break;
                case R.id.ll_repeat_month:
                    checkedRepeat = setClickedView(4);
                    break;
                case R.id.ll_repeat_year:
                    checkedRepeat = setClickedView(5);
                    break;
                case R.id.ll_cancel:
                    finish();
                    break;
                case R.id.btn_submit:
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("repeatInteger", checkedRepeat);
                    returnIntent.putExtra("repeatResult",getRepeatString(checkedRepeat));
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    break;
            }
        }
    };
    private int setClickedView(int ver){
        switch (ver){
            case 1:
                this.ivRepeatCheckNothing.setVisibility(View.VISIBLE);
                this.ivRepeatCheckDay.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckWeek.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckMonth.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckYear.setVisibility(View.INVISIBLE);
                this.llRepeatDayDetail.setVisibility(View.GONE);
                this.llRepeatWeekDetail.setVisibility(View.GONE);
                this.llRepeatMonthDetail.setVisibility(View.GONE);
                this.llRepeatYearDetail.setVisibility(View.GONE);

                break;
            case 2:
                this.ivRepeatCheckNothing.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckDay.setVisibility(View.VISIBLE);
                this.ivRepeatCheckWeek.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckMonth.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckYear.setVisibility(View.INVISIBLE);
                this.llRepeatDayDetail.setVisibility(View.VISIBLE);
                this.llRepeatWeekDetail.setVisibility(View.GONE);
                this.llRepeatMonthDetail.setVisibility(View.GONE);
                this.llRepeatYearDetail.setVisibility(View.GONE);
                break;
            case 3:
                this.ivRepeatCheckNothing.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckDay.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckWeek.setVisibility(View.VISIBLE);
                this.ivRepeatCheckMonth.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckYear.setVisibility(View.INVISIBLE);
                this.llRepeatDayDetail.setVisibility(View.GONE);
                this.llRepeatWeekDetail.setVisibility(View.VISIBLE);
                this.llRepeatMonthDetail.setVisibility(View.GONE);
                this.llRepeatYearDetail.setVisibility(View.GONE);
                break;
            case 4:
                this.ivRepeatCheckNothing.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckDay.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckWeek.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckMonth.setVisibility(View.VISIBLE);
                this.ivRepeatCheckYear.setVisibility(View.INVISIBLE);
                this.llRepeatDayDetail.setVisibility(View.GONE);
                this.llRepeatWeekDetail.setVisibility(View.GONE);
                this.llRepeatMonthDetail.setVisibility(View.VISIBLE);
                this.llRepeatYearDetail.setVisibility(View.GONE);
                break;
            case 5:
                this.ivRepeatCheckNothing.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckDay.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckWeek.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckMonth.setVisibility(View.INVISIBLE);
                this.ivRepeatCheckYear.setVisibility(View.VISIBLE);
                this.llRepeatDayDetail.setVisibility(View.GONE);
                this.llRepeatWeekDetail.setVisibility(View.GONE);
                this.llRepeatMonthDetail.setVisibility(View.GONE);
                this.llRepeatYearDetail.setVisibility(View.VISIBLE);
                break;
        }
        return ver;
    }

    private void bindingView(){

        this.llRepeatNothing = findViewById(R.id.ll_repeat_nothing);
        this.llRepeatNothing.setOnClickListener(listener);
        this.llRepeatDay = findViewById(R.id.ll_repeat_day);
        this.llRepeatDay.setOnClickListener(listener);
        this.llRepeatWeek = findViewById(R.id.ll_repeat_week);
        this.llRepeatWeek.setOnClickListener(listener);
        this.llRepeatMonth = findViewById(R.id.ll_repeat_month);
        this.llRepeatMonth.setOnClickListener(listener);
        this.llRepeatYear = findViewById(R.id.ll_repeat_year);
        this.llRepeatYear.setOnClickListener(listener);

        this.llRepeatDayDetail = findViewById(R.id.ll_repeat_day_detail);
        this.llRepeatWeekDetail = findViewById(R.id.ll_repeat_week_detail);
        this.llRepeatMonthDetail = findViewById(R.id.ll_repeat_month_detail);
        this.llRepeatYearDetail = findViewById(R.id.ll_repeat_year_detail);

        this.ivRepeatCheckNothing = findViewById(R.id.iv_repeat_check_nothing);
        this.ivRepeatCheckDay = findViewById(R.id.iv_repeat_check_day);
        this.ivRepeatCheckWeek = findViewById(R.id.iv_repeat_check_week);
        this.ivRepeatCheckMonth = findViewById(R.id.iv_repeat_check_month);
        this.ivRepeatCheckYear = findViewById(R.id.iv_repeat_check_year);

        this.btnSubmit = findViewById(R.id.btn_submit);
        this.llCancel = findViewById(R.id.ll_cancel);
        this.btnSubmit.setOnClickListener(listener);
        this.llCancel.setOnClickListener(listener);

    }
}
