package com.pkteam.smartcalendar.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
    private LinearLayout llCancel;
    private Button btnSubmit;

    // repeat information
    private String repeatMode;
    private Integer repeatModeInt;
    private Integer repeatPeriod;
    private Integer repeatTimes;

    private EditText etRepeatPeriod1, etRepeatTimes1;
    private EditText etRepeatPeriod2, etRepeatTimes2;
    private EditText etRepeatPeriod3, etRepeatTimes3;
    private EditText etRepeatPeriod4, etRepeatTimes4;


    private CheckBox checkbox1_1, checkbox1_2;
    private CheckBox checkbox2_1, checkbox2_2;
    private CheckBox checkbox3_1, checkbox3_2;
    private CheckBox checkbox4_1, checkbox4_2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_additem_repeat);
        bindingView();

        Intent intent = getIntent();
        this.repeatMode = intent.getStringExtra("repeatMode");
        repeatModeInt = setClickedView(getRepeatInteger(this.repeatMode));

    }





    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_repeat_nothing:
                    repeatModeInt = setClickedView(1);
                    break;
                case R.id.ll_repeat_day:
                    repeatModeInt = setClickedView(2);
                    break;
                case R.id.ll_repeat_week:
                    repeatModeInt = setClickedView(3);
                    break;
                case R.id.ll_repeat_month:
                    repeatModeInt = setClickedView(4);
                    break;
                case R.id.ll_repeat_year:
                    repeatModeInt = setClickedView(5);
                    break;
                case R.id.ll_cancel:
                    finish();
                    break;
                case R.id.btn_submit:
                    Toast.makeText(AddItemActivityRepeat.this, String.valueOf(repeatModeInt)+","+String.valueOf(repeatPeriod)+","+String.valueOf(repeatTimes), Toast.LENGTH_SHORT).show();

                    /*
                    if(checkException()){
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("repeatInteger", repeatModeInt);
                        returnIntent.putExtra("repeatResult",getRepeatString(repeatModeInt));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                    */

                    break;
            }
        }
    };


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

        // Detail 수정
        this.checkbox1_1 = findViewById(R.id.repeat_detail_1_cb1);
        this.checkbox1_2 = findViewById(R.id.repeat_detail_1_cb2);
        this.checkbox2_1 = findViewById(R.id.repeat_detail_2_cb1);
        this.checkbox2_2 = findViewById(R.id.repeat_detail_2_cb2);
        this.checkbox3_1 = findViewById(R.id.repeat_detail_3_cb1);
        this.checkbox3_2 = findViewById(R.id.repeat_detail_3_cb2);
        this.checkbox4_1 = findViewById(R.id.repeat_detail_4_cb1);
        this.checkbox4_2 = findViewById(R.id.repeat_detail_4_cb2);
        this.etRepeatPeriod1 = findViewById(R.id.repeat_detail_1_et1);
        this.etRepeatPeriod2 = findViewById(R.id.repeat_detail_2_et1);
        this.etRepeatPeriod3 = findViewById(R.id.repeat_detail_3_et1);
        this.etRepeatPeriod4 = findViewById(R.id.repeat_detail_4_et1);
        this.etRepeatTimes1 = findViewById(R.id.repeat_detail_1_et2);
        this.etRepeatTimes2 = findViewById(R.id.repeat_detail_2_et2);
        this.etRepeatTimes3 = findViewById(R.id.repeat_detail_3_et2);
        this.etRepeatTimes4 = findViewById(R.id.repeat_detail_4_et2);

        getInput();

    }


    private void getInput(){

        // Edit Text 설정
        // Default 값 : 반복주기 1, 반복종료 없음(0)
        repeatPeriod = 1;
        repeatTimes = 0;
        setEditTextPeriod(etRepeatPeriod1);
        setEditTextPeriod(etRepeatPeriod2);
        setEditTextPeriod(etRepeatPeriod3);
        setEditTextPeriod(etRepeatPeriod4);
        setEditTextTimes(etRepeatTimes1);
        setEditTextTimes(etRepeatTimes2);
        setEditTextTimes(etRepeatTimes3);
        setEditTextTimes(etRepeatTimes4);

        this.checkbox1_1.setClickable(false);
        this.checkbox2_1.setClickable(false);
        this.checkbox3_1.setClickable(false);
        this.checkbox4_1.setClickable(false);
        this.etRepeatTimes1.setEnabled(false);
        this.etRepeatTimes2.setEnabled(false);
        this.etRepeatTimes3.setEnabled(false);
        this.etRepeatTimes4.setEnabled(false);

        // CheckBox 설정

        // 매일
        this.checkbox1_1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 0;
                    buttonView.setClickable(false);
                    checkbox1_2.setClickable(true);
                    checkbox1_2.performClick();
                    etRepeatTimes1.setEnabled(false);
                }
            }
        });

        this.checkbox1_2.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 10;
                    buttonView.setClickable(false);
                    checkbox1_1.setClickable(true);
                    checkbox1_1.performClick();
                    etRepeatTimes1.setEnabled(true);
                }
            }
        });

        // 매주
        this.checkbox2_1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 0;
                    buttonView.setClickable(false);
                    checkbox2_2.setClickable(true);
                    checkbox2_2.performClick();
                    etRepeatTimes2.setEnabled(false);
                }
            }
        });

        this.checkbox2_2.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 10;
                    buttonView.setClickable(false);
                    checkbox2_1.setClickable(true);
                    checkbox2_1.performClick();
                    etRepeatTimes2.setEnabled(true);
                }
            }
        });

        // 매월
        this.checkbox3_1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 0;
                    buttonView.setClickable(false);
                    checkbox3_2.setClickable(true);
                    checkbox3_2.performClick();
                    etRepeatTimes3.setEnabled(false);
                }
            }
        });

        this.checkbox3_2.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 10;
                    buttonView.setClickable(false);
                    checkbox3_1.setClickable(true);
                    checkbox3_1.performClick();
                    etRepeatTimes3.setEnabled(true);
                }
            }
        });

        // 매년
        this.checkbox4_1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 0;
                    buttonView.setClickable(false);
                    checkbox4_2.setClickable(true);
                    checkbox4_2.performClick();
                    etRepeatTimes4.setEnabled(false);
                }
            }
        });

        this.checkbox4_2.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 10;
                    buttonView.setClickable(false);
                    checkbox4_1.setClickable(true);
                    checkbox4_1.performClick();
                    etRepeatTimes4.setEnabled(true);
                }
            }
        });

    }

    private void setEditTextPeriod(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    repeatPeriod = Integer.parseInt(s.toString());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void setEditTextTimes(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    repeatTimes = Integer.parseInt(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean checkException(){
        if (repeatTimes < 1){
            Toast.makeText(getApplicationContext(), "1이상의 숫자로 반복이 가능합니다.", Toast.LENGTH_SHORT).show();
        }

        return true;
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
}
