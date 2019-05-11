package com.pkteam.smartcalendar.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.pkteam.smartcalendar.databinding.ActivityAddItemRepeatBinding;
import com.singh.daman.gentletoast.GentleToast;

/**
 * Created by paeng on 2018. 7. 11..
 */

public class AddItemActivityRepeat extends AppCompatActivity {

    // 넘기는 데이터
    private Integer repeatModeInt;
    private Integer repeatPeriod;
    private Integer repeatTimes;

    ActivityAddItemRepeatBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_item_repeat);
        binding.setAdditem(this);
        bindingView();

        Intent intent = getIntent();
        repeatModeInt = setClickedView(getRepeatInteger(intent.getStringExtra("repeatMode")));

    }


    private void bindingView(){

        binding.llRepeatNothing.setOnClickListener(listener);
        binding.llRepeatDay.setOnClickListener(listener);
        binding.llRepeatWeek.setOnClickListener(listener);
        binding.llRepeatMonth.setOnClickListener(listener);
        binding.llRepeatYear.setOnClickListener(listener);

        getInput();
    }

    private void getInput(){

        // Edit Text 설정
        // Default 값 : 반복주기 1, 반복종료 없음(0)
        repeatPeriod = 1;
        repeatTimes = 0;
        setEditTextPeriod(binding.repeatDetail1Et1);
        setEditTextPeriod(binding.repeatDetail2Et1);
        setEditTextPeriod(binding.repeatDetail3Et1);
        setEditTextPeriod(binding.repeatDetail4Et1);
        setEditTextTimes(binding.repeatDetail1Et2);
        setEditTextTimes(binding.repeatDetail2Et2);
        setEditTextTimes(binding.repeatDetail3Et2);
        setEditTextTimes(binding.repeatDetail4Et2);

        binding.repeatDetail1Cb1.setClickable(false);
        binding.repeatDetail2Cb1.setClickable(false);
        binding.repeatDetail3Cb1.setClickable(false);
        binding.repeatDetail4Cb1.setClickable(false);
        binding.repeatDetail1Et2.setEnabled(false);
        binding.repeatDetail2Et2.setEnabled(false);
        binding.repeatDetail3Et2.setEnabled(false);
        binding.repeatDetail4Et2.setEnabled(false);

        // 매일
        binding.repeatDetail1Cb1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 0;
                    buttonView.setClickable(false);
                    binding.repeatDetail1Cb2.setClickable(true);
                    binding.repeatDetail1Cb2.performClick();
                    binding.repeatDetail1Et2.setEnabled(false);
                }
            }
        });

        binding.repeatDetail1Cb2.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 10;
                    buttonView.setClickable(false);
                    binding.repeatDetail1Cb1.setClickable(true);
                    binding.repeatDetail1Cb1.performClick();
                    binding.repeatDetail1Et2.setEnabled(true);
                }
            }
        });

        // 매주
        binding.repeatDetail2Cb1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 0;
                    buttonView.setClickable(false);
                    binding.repeatDetail2Cb2.setClickable(true);
                    binding.repeatDetail2Cb2.performClick();
                    binding.repeatDetail2Et2.setEnabled(false);
                }
            }
        });

        binding.repeatDetail2Cb2.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 10;
                    buttonView.setClickable(false);
                    binding.repeatDetail2Cb1.setClickable(true);
                    binding.repeatDetail2Cb1.performClick();
                    binding.repeatDetail2Et2.setEnabled(true);
                }
            }
        });

        // 매월
        binding.repeatDetail3Cb1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 0;
                    buttonView.setClickable(false);
                    binding.repeatDetail3Cb2.setClickable(true);
                    binding.repeatDetail3Cb2.performClick();
                    binding.repeatDetail3Et2.setEnabled(false);
                }
            }
        });

        binding.repeatDetail3Cb2.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 10;
                    buttonView.setClickable(false);
                    binding.repeatDetail3Cb1.setClickable(true);
                    binding.repeatDetail3Cb1.performClick();
                    binding.repeatDetail3Et2.setEnabled(true);
                }
            }
        });

        // 매년
        binding.repeatDetail4Cb1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 0;
                    buttonView.setClickable(false);
                    binding.repeatDetail4Cb2.setClickable(true);
                    binding.repeatDetail4Cb2.performClick();
                    binding.repeatDetail4Et2.setEnabled(false);
                }
            }
        });

        binding.repeatDetail4Cb2.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatTimes = 10;
                    buttonView.setClickable(false);
                    binding.repeatDetail4Cb1.setClickable(true);
                    binding.repeatDetail4Cb1.performClick();
                    binding.repeatDetail4Et2.setEnabled(true);
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
        if (repeatPeriod < 1){
            GentleToast.with(getApplicationContext()).longToast("1이상의 주기로 반복이 가능합니다.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
            return false;
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
                GentleToast.with(getApplicationContext()).longToast("Error Code : 1112").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();

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
                GentleToast.with(getApplicationContext()).longToast("Error Code : 1112").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                break;
        }
        return returnString;
    }

    private int setClickedView(int ver){
        if (ver == 1){
            binding.ivRepeatCheckNothing.setVisibility(View.VISIBLE);
        }else{
            binding.ivRepeatCheckNothing.setVisibility(View.INVISIBLE);
        }
        if (ver == 2) {
            binding.ivRepeatCheckDay.setVisibility(View.VISIBLE);
            binding.llRepeatDayDetail.setVisibility(View.VISIBLE);
        }else{
            binding.ivRepeatCheckDay.setVisibility(View.INVISIBLE);
            binding.llRepeatDayDetail.setVisibility(View.GONE);
        }
        if (ver == 3) {
            binding.ivRepeatCheckWeek.setVisibility(View.VISIBLE);
            binding.llRepeatWeekDetail.setVisibility(View.VISIBLE);
        }else{
            binding.ivRepeatCheckWeek.setVisibility(View.INVISIBLE);
            binding.llRepeatWeekDetail.setVisibility(View.GONE);
        }
        if (ver == 4) {
            binding.ivRepeatCheckMonth.setVisibility(View.VISIBLE);
            binding.llRepeatMonthDetail.setVisibility(View.VISIBLE);
        }else{
            binding.ivRepeatCheckMonth.setVisibility(View.INVISIBLE);
            binding.llRepeatMonthDetail.setVisibility(View.GONE);
        }
        if (ver == 5) {
            binding.ivRepeatCheckYear.setVisibility(View.VISIBLE);
            binding.llRepeatYearDetail.setVisibility(View.VISIBLE);
        }else{
            binding.ivRepeatCheckYear.setVisibility(View.INVISIBLE);
            binding.llRepeatYearDetail.setVisibility(View.GONE);
        }
        return ver;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_repeat_nothing:
                    repeatModeInt = setClickedView(1);
                    repeatPeriod = 1;
                    repeatTimes = 0;
                    break;
                case R.id.ll_repeat_day:
                    repeatModeInt = setClickedView(2);
                    repeatPeriod = Integer.valueOf(binding.repeatDetail1Et1.getText().toString());
                    if (binding.repeatDetail1Cb1.isChecked()){
                        repeatTimes = 0;
                    }else{
                        repeatTimes = Integer.valueOf(binding.repeatDetail1Et2.getText().toString());
                    }
                    break;
                case R.id.ll_repeat_week:
                    repeatModeInt = setClickedView(3);
                    repeatPeriod = Integer.valueOf(binding.repeatDetail2Et1.getText().toString());
                    if (binding.repeatDetail2Cb1.isChecked()){
                        repeatTimes = 0;
                    }else{
                        repeatTimes = Integer.valueOf(binding.repeatDetail2Et2.getText().toString());
                    }
                    break;
                case R.id.ll_repeat_month:
                    repeatModeInt = setClickedView(4);
                    repeatPeriod = Integer.valueOf(binding.repeatDetail3Et1.getText().toString());
                    if (binding.repeatDetail3Cb1.isChecked()){
                        repeatTimes = 0;
                    }else{
                        repeatTimes = Integer.valueOf(binding.repeatDetail3Et2.getText().toString());
                    }
                    break;
                case R.id.ll_repeat_year:
                    repeatModeInt = setClickedView(5);
                    repeatPeriod = Integer.valueOf(binding.repeatDetail4Et1.getText().toString());
                    if (binding.repeatDetail4Cb1.isChecked()){
                        repeatTimes = 0;
                    }else{
                        repeatTimes = Integer.valueOf(binding.repeatDetail4Et2.getText().toString());
                    }
                    break;
            }
        }
    };

    public void onSubmit(View view){
        // (RepeatMode, RepeatPeriod, RepeatTimes)
        if(checkException()){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("repeatModeString", getRepeatString(repeatModeInt));
            returnIntent.putExtra("repeatMode", repeatModeInt);
            returnIntent.putExtra("repeatPeriod", repeatPeriod);
            returnIntent.putExtra("repeatTimes", repeatTimes);

            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    public void finishView(View view){
        finish();
    }
}
