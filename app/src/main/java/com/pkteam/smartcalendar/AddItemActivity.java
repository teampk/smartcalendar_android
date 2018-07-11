package com.pkteam.smartcalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.simmorsal.library.ConcealerNestedScrollView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
 * Created by paeng on 2018. 7. 5..
 */

public class AddItemActivity extends AppCompatActivity {

    private static final int STATIC_MODE = 1111;
    private static final int DYNAMIC_MODE = 1112;
    private static final int ALL_DAY_MODE = 1113;
    private static final int NOT_ALL_DAY_MODE = 1114;

    private static final int ADD_MODE = 111;
    private static final int EDIT_MODE = 112;

    private static final int REQUEST_REPEAT = 11;


    ConcealerNestedScrollView concealerNSV;
    CardView crdHeaderView;
    LinearLayout linFooterView;

    Button btnCancel, btnSubmit;

    EditText etTitle, etLoc, etMemo;
    TextView tvStaticDynamic;
    Switch swStaticDynamic, swAllday;

    // Date and Time picker
    LinearLayout llTimeStart, llTimeEnd, llTimeDeadline, llStatic, llDynamic, llRepeat, llCategory;
    TextView tvTimeStart, tvTimeEnd, tvDateStart, tvDateEnd, tvDateDeadline, tvTimeDeadline, tvRepeat;
    SingleDateAndTimePickerDialog.Builder singleBuilder;
    SimpleDateFormat simpleTimeFormat;
    String[] dateString;
    private int modeStaticDynamic;
    private int modeAllDay;
    private int addEdit;


    // 1.id(Int)    2.title(String)  3.loc(String)   4.isStatic(boolean)  5.isAllday(boolean)
    // 6.time(String)   7.repeatId(Int)     8.category(Int)     9.Memo(String)  10.NeedTime(int)
    private int item1_id;
    private String item2_title;
    private String item3_loc;
    private boolean item4_isDynamic;
    private boolean item5_isAllDay;
    private String item6_time;
    private int item7_repeatId;
    private int item8_category;
    private String item9_Memo;
    private int item10_needTime;

    private int repeatMode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_additem);
        bindingView();
        initSet(false);
        setupConcealerNSV();
    }

    public void initSet(boolean isEdit){

        // 수정하는 경우
        if(isEdit){
            linFooterView.setVisibility(View.VISIBLE);
        }
        // 추가하는 경우
        else{
            linFooterView.setVisibility(View.GONE);
            modeStaticDynamic = STATIC_MODE;
            modeAllDay = NOT_ALL_DAY_MODE;
            String date[] = getCurrentDate().split("/");
            tvDateStart.setText(date[0]);
            tvTimeStart.setText(date[1]);
            tvDateEnd.setText(date[0]);
            tvTimeEnd.setText(date[1]);
            tvDateDeadline.setText(date[0]);
            tvTimeDeadline.setText(date[1]);
        }



        swStaticDynamic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){              // Dynamic State
                    setSwitchToDynamic();

                }else{                      // Static State
                    setSwitchToStatic();
                }
            }
        });
        swAllday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    setSwitchToAllDay();
                }else{
                    setSwitchToNotAllDay();
                }
            }
        });
    }

    // for Switch
    private void setSwitchToDynamic(){
        llStatic.setVisibility(View.GONE);
        llDynamic.setVisibility(View.VISIBLE);
        tvStaticDynamic.setText("Dynamic");
        modeStaticDynamic = DYNAMIC_MODE;
    }

    private void setSwitchToStatic(){
        llStatic.setVisibility(View.VISIBLE);
        llDynamic.setVisibility(View.GONE);
        tvStaticDynamic.setText("Static");
        modeStaticDynamic = STATIC_MODE;
    }

    private void setSwitchToAllDay(){
        tvTimeStart.setVisibility(View.GONE);
        tvTimeEnd.setVisibility(View.GONE);
        modeAllDay = ALL_DAY_MODE;
    }

    private void setSwitchToNotAllDay(){
        tvTimeStart.setVisibility(View.VISIBLE);
        tvTimeEnd.setVisibility(View.VISIBLE);
        modeAllDay = NOT_ALL_DAY_MODE;
    }

    private void setupConcealerNSV() {
        // if you're setting header and footer views at the very start of the layout creation (onCreate),
        // as the views are not drawn yet, the library cant get their correct sizes, so you have to do this:

        crdHeaderView.post(new Runnable() {
            @Override
            public void run() {
                concealerNSV.setHeaderView(crdHeaderView, 0);
            }
        });
        linFooterView.post(new Runnable() {
            @Override
            public void run() {
                concealerNSV.setFooterView(linFooterView, 0);
            }
        });

        // pass a true to setHeaderFastHide to make views hide faster
        concealerNSV.setHeaderFastHide(true);
    }

    public void selectDateAndTime(int ver){

        Calendar calendar = Calendar.getInstance();
        String date[] = getCurrentDate().split("/");

        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(date[1].split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(date[1].split(":")[1]));

        final Date defaultDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(AddItemActivity.this)

                .bottomSheet()
                .curved()
                .defaultDate(defaultDate);

                //.titleTextColor(Color.GREEN)
                //.backgroundColor(Color.BLACK)
                //.mainColor(Color.GREEN)
                if (ver==1||ver==2||ver==3){
                    singleBuilder.displayMinutes(true);
                    singleBuilder.displayHours(true);
                }else{
                    singleBuilder.displayMinutes(false);
                    singleBuilder.displayHours(false);
                }

        singleBuilder
                .displayDays(true)
                .displayMonth(false)
                .displayDaysOfMonth(false)
                .displayYears(false)
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {

                    }
                })

                .title("날짜와 시간을 고르세요");

        if (ver==1) {
            //YYYY.MM.dd/HH:mm
            singleBuilder.listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {
                    dateString = simpleTimeFormat.format(date).split("/");
                    tvDateStart.setText(dateString[0]);
                    tvTimeStart.setText(dateString[1]);

                }
            });
        }else if(ver==2){
            singleBuilder.listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {
                    dateString = simpleTimeFormat.format(date).split("/");
                    tvDateEnd.setText(dateString[0]);
                    tvTimeEnd.setText(dateString[1]);

                }
            });
        }else if(ver==3){
            singleBuilder.listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {
                    dateString = simpleTimeFormat.format(date).split("/");
                    tvDateDeadline.setText(dateString[0]);
                    tvTimeDeadline.setText(dateString[1]);
                }
            });
        }else if(ver==4){
            singleBuilder.listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {
                    dateString = simpleTimeFormat.format(date).split("/");
                    tvDateStart.setText(dateString[0]);
                }
            });
        }else if(ver==5){
            singleBuilder.listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {
                    dateString = simpleTimeFormat.format(date).split("/");
                    tvDateEnd.setText(dateString[0]);
                }
            });
        }
        singleBuilder.display();

    }

    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_add:
                    // 1.id(Int)    2.title(String)  3.loc(String)   4.isStatic(boolean)  5.isAllday(boolean)
                    // 6.time(String)   7.repeatId(Int)     8.category(Int)     9.Memo(String)  10.NeedTime(int)
                    if (checkItem()){
                        //2
                        item2_title = etTitle.getText().toString();
                        //3
                        item3_loc = etLoc.getText().toString();
                        //4, 5
                        if (modeStaticDynamic == STATIC_MODE){
                            item4_isDynamic = false;
                            if (modeAllDay == ALL_DAY_MODE){
                                item5_isAllDay = true;
                                //201807110000.201807120000.000000000000
                                item6_time = getTimeData(tvDateStart)+"0000."
                                            +getTimeData(tvDateEnd)+"0000.000000000000";
                            }else if (modeAllDay == NOT_ALL_DAY_MODE){
                                item5_isAllDay = false;
                                //201807110430.201807120630.000000000000
                                item6_time = getTimeData(tvDateStart)
                                        +getTimeData(tvTimeStart)
                                        +"."+getTimeData(tvDateEnd)
                                        +getTimeData(tvTimeEnd)
                                        +".000000000000";

                            }
                        }else if (modeStaticDynamic == DYNAMIC_MODE){
                            item4_isDynamic = true;
                            item5_isAllDay = false;
                            //000000000000.000000000000.201807120630
                            item6_time = "000000000000.000000000000."
                                    +getTimeData(tvDateDeadline)+getTimeData(tvTimeDeadline);
                        }
                        item9_Memo = etMemo.getText().toString();

                    }
                    String testing = item2_title + item3_loc + String.valueOf(item4_isDynamic) + String.valueOf(item5_isAllDay)+"\n"+item6_time;


                    Toast.makeText(getApplicationContext(), testing, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_cancel:
                    finish();
                    break;
                case R.id.ll_time_start:
                    if (modeAllDay == ALL_DAY_MODE){
                        selectDateAndTime(4);
                    }else{
                        selectDateAndTime(1);
                    }
                    break;
                case R.id.ll_time_end:
                    if (modeAllDay == ALL_DAY_MODE){
                        selectDateAndTime(5);
                    }else{
                        selectDateAndTime(2);
                    }
                    break;
                case R.id.ll_time_deadline:
                    selectDateAndTime(3);
                    break;
                case R.id.ll_repeat:
                    Intent intentRepeat = new Intent(AddItemActivity.this, AddItemActivityRepeat.class);
                    intentRepeat.putExtra("repeatMode", tvRepeat.getText().toString());
                    startActivityForResult(intentRepeat, REQUEST_REPEAT);
                    break;
                case R.id.ll_category:
                    Intent intentCategory = new Intent(AddItemActivity.this, AddItemActivityCategory.class);
                    startActivity(intentCategory);
                    break;

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_REPEAT) {
            if(resultCode == Activity.RESULT_OK){
                tvRepeat.setText(data.getStringExtra("repeatResult"));
                data.getIntExtra("repeatResultInteger", repeatMode);
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("ActivityResult", "CANCELED");
            }
        }
    }//onActivityResult

    public String getCurrentDate(){
        // get Current Date and Time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd/HH:mm");
        return sdf.format(date);
    }

    private void bindingView(){
        // Header, Footer
        concealerNSV = findViewById(R.id.concealerNSV);
        crdHeaderView = findViewById(R.id.crdHeaderView);
        linFooterView = findViewById(R.id.linFooterView);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(listener);
        btnSubmit = findViewById(R.id.btn_add);
        btnSubmit.setOnClickListener(listener);
        //
        etTitle = findViewById(R.id.et_title);
        etLoc = findViewById(R.id.et_loc);
        etMemo = findViewById(R.id.et_memo);
        tvStaticDynamic = findViewById(R.id.tv_static_dynamic);
        swStaticDynamic = findViewById(R.id.sw_static_dynamic);
        llStatic = findViewById(R.id.ll_static);
        llDynamic = findViewById(R.id.ll_dynamic);

        // 날짜 시간 선택
        swAllday = findViewById(R.id.sw_all_day);
        llTimeStart = findViewById(R.id.ll_time_start);
        llTimeEnd = findViewById(R.id.ll_time_end);
        llTimeDeadline = findViewById(R.id.ll_time_deadline);
        tvDateStart = findViewById(R.id.tv_date_start);
        tvTimeStart = findViewById(R.id.tv_time_start);
        tvDateEnd = findViewById(R.id.tv_date_end);
        tvTimeEnd = findViewById(R.id.tv_time_end);
        tvDateDeadline = findViewById(R.id.tv_date_deadline);
        tvTimeDeadline = findViewById(R.id.tv_time_deadline);
        llRepeat = findViewById(R.id.ll_repeat);
        llCategory = findViewById(R.id.ll_category);

        llTimeStart.setOnClickListener(listener);
        llTimeEnd.setOnClickListener(listener);
        llTimeDeadline.setOnClickListener(listener);
        llRepeat.setOnClickListener(listener);
        llCategory.setOnClickListener(listener);
        this.simpleTimeFormat = new SimpleDateFormat("YYYY.MM.dd/HH:mm", Locale.getDefault());

        tvRepeat = findViewById(R.id.tv_repeat);



    }

    private boolean checkItem(){
        if (etTitle.getText().toString().replace(" ", "").equals("")) {
            Toast.makeText(AddItemActivity.this, "제목을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    public String replaceSS(String inputString){
        String outputString = inputString.replace(".","").replace(":","");
        return outputString;
    }

    public String getTimeData(TextView tv){
        return tv.getText().toString().replace(".","").replace(":","");
    }

}
