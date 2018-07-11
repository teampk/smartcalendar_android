package com.pkteam.smartcalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by paeng on 2018. 7. 11..
 */

public class AddItemActivityRepeat extends AppCompatActivity {
    private LinearLayout llRepeatNothing, llRepeatDay, llRepeatWeek, llRepeatMonth, llRepeatYear;
    private ImageView ivRepeatCheckNothing, ivRepeatCheckDay, ivRepeatCheckWeek, ivRepeatCheckMonth, ivRepeatCheckYear;
    private int checkedRepeat;
    private LinearLayout llCancel;
    private Button btnSubmit;
    private String repeatMode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    returnIntent.putExtra("repeatResultInteger", checkedRepeat);
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
                ivRepeatCheckNothing.setVisibility(View.VISIBLE);
                ivRepeatCheckDay.setVisibility(View.INVISIBLE);
                ivRepeatCheckWeek.setVisibility(View.INVISIBLE);
                ivRepeatCheckMonth.setVisibility(View.INVISIBLE);
                ivRepeatCheckYear.setVisibility(View.INVISIBLE);
                break;
            case 2:
                ivRepeatCheckNothing.setVisibility(View.INVISIBLE);
                ivRepeatCheckDay.setVisibility(View.VISIBLE);
                ivRepeatCheckWeek.setVisibility(View.INVISIBLE);
                ivRepeatCheckMonth.setVisibility(View.INVISIBLE);
                ivRepeatCheckYear.setVisibility(View.INVISIBLE);
                break;
            case 3:
                ivRepeatCheckNothing.setVisibility(View.INVISIBLE);
                ivRepeatCheckDay.setVisibility(View.INVISIBLE);
                ivRepeatCheckWeek.setVisibility(View.VISIBLE);
                ivRepeatCheckMonth.setVisibility(View.INVISIBLE);
                ivRepeatCheckYear.setVisibility(View.INVISIBLE);
                break;
            case 4:
                ivRepeatCheckNothing.setVisibility(View.INVISIBLE);
                ivRepeatCheckDay.setVisibility(View.INVISIBLE);
                ivRepeatCheckWeek.setVisibility(View.INVISIBLE);
                ivRepeatCheckMonth.setVisibility(View.VISIBLE);
                ivRepeatCheckYear.setVisibility(View.INVISIBLE);
                break;
            case 5:
                ivRepeatCheckNothing.setVisibility(View.INVISIBLE);
                ivRepeatCheckDay.setVisibility(View.INVISIBLE);
                ivRepeatCheckWeek.setVisibility(View.INVISIBLE);
                ivRepeatCheckMonth.setVisibility(View.INVISIBLE);
                ivRepeatCheckYear.setVisibility(View.VISIBLE);
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
