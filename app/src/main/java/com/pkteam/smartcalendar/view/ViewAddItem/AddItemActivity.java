package com.pkteam.smartcalendar.view.ViewAddItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.model.MyData;
import com.simmorsal.library.ConcealerNestedScrollView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private static final int ADD_MODE = 1;
    private static final int EDIT_MODE = 2;

    private static final int REQUEST_REPEAT = 11;
    private static final int REQUEST_CATEGORY = 12;

    private static final int REPEATAMOUNT = 10;

    private DBHelper dbHelper;

    ConcealerNestedScrollView concealerNSV;
    CardView crdHeaderView;
    LinearLayout linFooterView;

    Button btnCancel, btnSubmit;

    EditText etTitle, etLoc, etMemo, etNeedTime;
    TextView tvStaticDynamic;
    Switch swStaticDynamic, swAllday;

    // Date and Time picker
    LinearLayout llTimeStart, llTimeEnd, llTimeDeadline, llStatic, llDynamic, llRepeat, llCategory, llRepeatTotal;
    TextView tvTimeStart, tvTimeEnd, tvDateStart, tvDateEnd, tvDateDeadline, tvTimeDeadline, tvRepeat, tvCategory;
    SingleDateAndTimePickerDialog.Builder singleBuilder;
    SimpleDateFormat simpleTimeFormat;
    ImageView ivCategory;
    String[] dateString;
    private int modeStaticDynamic;
    private int modeAllDay;

    private int modeAddEdit;

    private MyData itemElement;

    // 0.id(Int)    1.title(String)  2.loc(String)   3.isStatic(boolean)  4.isAllday(boolean)
    // 5.time(String)    6.category(Int)     7.Memo(String)  8.NeedTime(int)    9.repeatId(Int)
    private int item0_id;
    private String item1_title;
    private String item2_loc;
    private boolean item3_isDynamic;
    private boolean item4_isAllDay;
    private String item5_time;
    private int item6_category;
    private String item7_Memo;
    private int item8_needTime;
    private int item9_repeatId;

    private int repeatMode, categoryMode;
    private int repeatPeriod, repeatTimes;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

    // for testing
    private TextView tvTopBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_additem);
        bindingView();
        Intent getIntent = getIntent();
        modeAddEdit = getIntent.getIntExtra("mode",0);
        int id = getIntent.getIntExtra("id", 0);
        initSet(modeAddEdit, id);
        setupConcealerNSV();
    }

    public void initSet(int isEdit, int dataId){
        modeStaticDynamic = STATIC_MODE;
        modeAllDay = NOT_ALL_DAY_MODE;
        categoryMode = 1;
        repeatMode = 1;
        repeatTimes = 0;
        item3_isDynamic = false;
        item4_isAllDay = false;

        // 추가하는 경우
        if(isEdit==ADD_MODE){
            etTitle.requestFocus();
            linFooterView.setVisibility(View.GONE);
            llRepeatTotal.setVisibility(View.VISIBLE);
            String date[] = getCurrentDate().split("/");
            tvDateStart.setText(date[0]);
            tvTimeStart.setText(date[1]);
            tvDateEnd.setText(date[0]);
            tvTimeEnd.setText(date[1]);
            tvDateDeadline.setText(date[0]);
            tvTimeDeadline.setText(date[1]);
        }
        // 수정하는 경우
        else if (isEdit==EDIT_MODE){
            linFooterView.setVisibility(View.VISIBLE);
            llRepeatTotal.setVisibility(View.GONE);
            setViewFromId(dataId);
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

    // 0.id(Int)    1.title(String)  2.loc(String)   3.isStatic(boolean)  4.isAllday(boolean)
    // 5.time(String)    6.category(Int)     7.Memo(String)  8.NeedTime(int)    9.repeatId(Int)
    // 수정하는 경우 데이터 받아오는 함수
    public void setViewFromId(int id){
        item0_id =id;
        itemElement = dbHelper.getDataById(id);

        etTitle.setText(itemElement.mTitle);
        etLoc.setText(itemElement.mLocation);
        swStaticDynamic.setChecked(itemElement.mIsDynamic);
        categoryMode = itemElement.mCategory;
        ArrayList<String> categoryList = dbHelper.getAllCategory();
        tvCategory.setText(categoryList.get(categoryMode - 1));
        ivCategory.setImageDrawable(getCategoryDrawable(itemElement.mCategory));

        etMemo.setText(itemElement.mMemo);
        String timeSplit[] = itemElement.mTime.split("\\.");

        if(!itemElement.mIsDynamic){
            setSwitchToStatic();
            tvDateStart.setText(getDateFromData(timeSplit[0]));
            tvDateEnd.setText(getDateFromData(timeSplit[1]));
            if(itemElement.mIsAllday){
                swAllday.setChecked(true);
                setSwitchToAllDay();
            }else{
                tvTimeStart.setText(getTimeFromData(timeSplit[0]));
                tvTimeEnd.setText(getTimeFromData(timeSplit[1]));
            }
            modeStaticDynamic = STATIC_MODE;
            item3_isDynamic = false;
        }else{
            setSwitchToDynamic();
            tvDateDeadline.setText(getDateFromData(timeSplit[2]));
            tvTimeDeadline.setText(getTimeFromData(timeSplit[2]));
            etNeedTime.setText(String.valueOf(itemElement.mNeedTime));
            modeStaticDynamic = DYNAMIC_MODE;
            item3_isDynamic = true;
        }

        // for testing
        tvTopBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), String.valueOf(itemElement.mRepeatId), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private String getDateFromData(String input){
        return input.substring(0,4)+"."+input.substring(4,6)+"."+input.substring(6,8);
    }

    @NonNull
    private String getTimeFromData(String input){
        return input.substring(8,10)+":"+input.substring(10,12);
    }

    // for Switch
    // Dynamic 체크 시
    private void setSwitchToDynamic(){
        llStatic.setVisibility(View.GONE);
        llDynamic.setVisibility(View.VISIBLE);
        repeatMode = 1;
        item3_isDynamic = true;
        tvStaticDynamic.setText(R.string.string_dynamic);
        modeStaticDynamic = DYNAMIC_MODE;
    }

    // Static 체크 시
    private void setSwitchToStatic(){
        llStatic.setVisibility(View.VISIBLE);
        llDynamic.setVisibility(View.GONE);
        repeatMode = getRepeatInteger(tvRepeat.getText().toString());
        item3_isDynamic = false;
        tvStaticDynamic.setText(R.string.string_static);
        modeStaticDynamic = STATIC_MODE;
    }

    private void setSwitchToAllDay(){
        tvTimeStart.setVisibility(View.GONE);
        tvTimeEnd.setVisibility(View.GONE);
        item4_isAllDay = true;
        modeAllDay = ALL_DAY_MODE;
    }

    private void setSwitchToNotAllDay(){
        tvTimeStart.setVisibility(View.VISIBLE);
        tvTimeEnd.setVisibility(View.VISIBLE);
        item4_isAllDay = false;
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
                .curved();
                //.defaultDate(defaultDate);
                //.titleTextColor(Color.GREEN)
                //.backgroundColor(Color.BLACK)
                //.mainColor(Color.GREEN)
                //.minDateRange()
                //.maxDateRange()
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
                    tvDateEnd.setText(dateString[0]);
                    tvTimeEnd.setText(dateString[1]);

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
                    tvDateEnd.setText(dateString[0]);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_REPEAT) {
            if(resultCode == Activity.RESULT_OK){
                tvRepeat.setText(data.getStringExtra("repeatModeString"));
                repeatMode = data.getIntExtra("repeatMode", 1);
                repeatPeriod = data.getIntExtra("repeatPeriod", 1);
                repeatTimes = data.getIntExtra("repeatTimes", 0);
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("ActivityResult", "CANCELED");
            }
        }else if (requestCode == REQUEST_CATEGORY){
            if(resultCode == Activity.RESULT_OK){
                tvCategory.setText(data.getStringExtra("categoryResult"));
                categoryMode = data.getIntExtra("categoryInteger", 1);
                ivCategory.setImageDrawable(getCategoryDrawable(categoryMode));
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("ActivityResult", "CANCELED");
            }
        }
    }

    public String getCurrentDate(){
        // get Current Date and Time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd/HH:mm");
        return sdf.format(date);
    }

    private Drawable getCategoryDrawable(int category){
        Drawable categoryDrawable = null;
        switch (category){
            case 1:
                categoryDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_category_1_24dp);
                break;
            case 2:
                categoryDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_category_2_24dp);
                break;
            case 3:
                categoryDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_category_3_24dp);
                break;
            case 4:
                categoryDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_category_4_24dp);
                break;
            case 5:
                categoryDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_category_5_24dp);
                break;
        }
        return categoryDrawable;

    }

    private boolean checkItem(){
        if (etTitle.getText().toString().replace(" ", "").equals("")) {
            Toast.makeText(AddItemActivity.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (etNeedTime.getText().toString().equals("") && item3_isDynamic){
            Toast.makeText(AddItemActivity.this, "필요시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (Double.parseDouble(getTimeData(tvDateStart)+getTimeData(tvTimeStart))>Double.parseDouble(getTimeData(tvDateEnd)+getTimeData(tvTimeEnd)) && !item3_isDynamic){
            Toast.makeText(AddItemActivity.this, "시작시간은 끝시간보다 빨라야 합니다.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            Log.d("CheckPaeng", getTimeData(tvDateStart)+getTimeData(tvTimeStart)+"//"+getTimeData(tvDateEnd)+getTimeData(tvTimeEnd)+"//"+String.valueOf(item3_isDynamic));
            return true;
        }
    }

    public String getTimeData(TextView tv){
        return tv.getText().toString().replace(".","").replace(":","");
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
                Toast.makeText(getApplicationContext(), "error occured in repeat", Toast.LENGTH_SHORT).show();
                break;
        }
        return mode;
    }

    // date 에 대한 milliseconds 구하기
    // mode 1 - 시작 시간
    // mode 2 - 끝 시간
    private long getMsByDate(String date, int mode){
        long dateForMs = 0;
        try {
            if (mode == 1){
                dateForMs = sdf.parse(date.split("\\.")[0]).getTime();
            }
            else if (mode == 2){
                dateForMs = sdf.parse(date.split("\\.")[1]).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateForMs;
    }

    private String getNextTimeByMs(long start, long end){
        Date mDate1 = new Date(start);
        Date mDate2 = new Date(end);
        String nextStart = sdf.format(mDate1);
        String nextEnd = sdf.format(mDate2);

        return String.valueOf(nextStart)+"."+String.valueOf(nextEnd)+".000000000000";
    }

    private String getNextMonth(String date){
        String startDate = getYearMonthString(date.split("\\.")[0]) + date.split("\\.")[0].substring(6);
        String endDate = getYearMonthString(date.split("\\.")[1]) + date.split("\\.")[1].substring(6);
        return startDate+"."+endDate+".000000000000";

    }

    private String getYearMonthString(String date){
        int nextYearMonthInt = Integer.valueOf(date.substring(0,6))+1;
        if ((nextYearMonthInt % 100)==13){
            return String.valueOf(Integer.valueOf(String.valueOf(nextYearMonthInt).substring(0,4))+1)+"01";
        }else{
            return String.valueOf(nextYearMonthInt);
        }

    }

    private String getNextYear(String date){
        String startDate = date.split("\\.")[0];
        String endDate = date.split("\\.")[1];
        String startDateNext = String.valueOf(Integer.valueOf(date.split("\\.")[0].substring(0,4))+1)+startDate.substring(4);
        String endDateNext = String.valueOf(Integer.valueOf(date.split("\\.")[1].substring(0,4))+1)+endDate.substring(4);
        return startDateNext+"."+endDateNext+".000000000000";
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
        linFooterView.setOnClickListener(listener);
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
        etNeedTime = findViewById(R.id.et_needtime);

        llRepeatTotal = findViewById(R.id.ll_repeat_total);

        llTimeStart.setOnClickListener(listener);
        llTimeEnd.setOnClickListener(listener);
        llTimeDeadline.setOnClickListener(listener);
        llRepeat.setOnClickListener(listener);
        llCategory.setOnClickListener(listener);
        this.simpleTimeFormat = new SimpleDateFormat("YYYY.MM.dd/HH:mm", Locale.getDefault());

        tvRepeat = findViewById(R.id.tv_repeat);
        tvCategory = findViewById(R.id.tv_category);
        ivCategory = findViewById(R.id.iv_category);
        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);
        tvCategory.setText(dbHelper.getAllCategory().get(0));

        // for testing
        tvTopBar = findViewById(R.id.tv_top_bar);
    }

    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_add:
                    // 0.id(Int)    1.title(String)  2.loc(String)   3.isStatic(boolean)  4.isAllday(boolean)
                    // 5.time(String)    6.category(Int)     7.Memo(String)  8.NeedTime(int)    9.repeatId(Int)
                    if (checkItem()){
                        item1_title = etTitle.getText().toString();
                        item2_loc = etLoc.getText().toString();
                        // Static Mode 일 때
                        if (modeStaticDynamic == STATIC_MODE){
                            item3_isDynamic = false;
                            // All Day Mode 일 때
                            if (modeAllDay == ALL_DAY_MODE){
                                item4_isAllDay = true;
                                //201807110000.201807120000.000000000000
                                item5_time = getTimeData(tvDateStart)+"0000."
                                        +getTimeData(tvDateEnd)+"0000.000000000000";
                            }
                            // All Day Mode 아닐 때
                            else if (modeAllDay == NOT_ALL_DAY_MODE){
                                item4_isAllDay = false;
                                //201807110430.201807120630.000000000000
                                item5_time = getTimeData(tvDateStart)
                                        +getTimeData(tvTimeStart)
                                        +"."+getTimeData(tvDateEnd)
                                        +getTimeData(tvTimeEnd)
                                        +".000000000000";
                            }
                            item8_needTime = 0;

                        }
                        // Dynamic Mode 일 때
                        else if (modeStaticDynamic == DYNAMIC_MODE){
                            item3_isDynamic = true;
                            item4_isAllDay = false;
                            //000000000000.000000000000.201807120630
                            item5_time = "000000000000.000000000000."
                                    +getTimeData(tvDateDeadline)+getTimeData(tvTimeDeadline);
                            item8_needTime = Integer.parseInt(etNeedTime.getText().toString());
                        }
                        else{
                            Toast.makeText(AddItemActivity.this, "에러가 발생했습니다. (ERROR CODE : 1111)", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        item6_category = categoryMode;
                        item7_Memo = etMemo.getText().toString();
                        long nextTime_s;
                        long nextTime_e;
                        if (modeAddEdit == ADD_MODE){
                            switch (repeatMode){

                                // 반복 없음
                                case 1:
                                    item9_repeatId = 0;
                                    dbHelper.todoDataInsert(item1_title, item2_loc, item3_isDynamic, item4_isAllDay, item5_time, item6_category, item7_Memo, item8_needTime, item9_repeatId);
                                    Toast.makeText(getApplicationContext(), "일정이 등록되었습니다", Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                // 매일 반복
                                case 2:
                                    dbHelper.updateRepeatId();
                                    item9_repeatId = dbHelper.getCurrentRepeatId();
                                    // item5_time : 201901070700.201901071000.000000000000

                                    dbHelper.todoDataInsert(item1_title, item2_loc, item3_isDynamic, item4_isAllDay, item5_time, item6_category, item7_Memo, item8_needTime, item9_repeatId);
                                    nextTime_s = getMsByDate(item5_time, 1);
                                    nextTime_e = getMsByDate(item5_time, 2);

                                    // 반복 종료가 없으면
                                    if (repeatTimes==0){
                                        repeatTimes = REPEATAMOUNT;
                                    }

                                    for (int i=0;i<repeatTimes-1;i++){
                                        nextTime_s += (86400000 * repeatPeriod);
                                        nextTime_e += (86400000 * repeatPeriod);
                                        dbHelper.todoDataInsert(item1_title, item2_loc, item3_isDynamic, item4_isAllDay, getNextTimeByMs(nextTime_s, nextTime_e), item6_category, item7_Memo, item8_needTime, item9_repeatId);
                                    }

                                    Toast.makeText(getApplicationContext(), "반복 일정이 등록되었습니다", Toast.LENGTH_SHORT).show();

                                    finish();
                                    break;
                                // 매주 반복
                                case 3:
                                    dbHelper.updateRepeatId();
                                    item9_repeatId = dbHelper.getCurrentRepeatId();
                                    // item5_time : 201901070700.201901071000.000000000000

                                    dbHelper.todoDataInsert(item1_title, item2_loc, item3_isDynamic, item4_isAllDay, item5_time, item6_category, item7_Memo, item8_needTime, item9_repeatId);
                                    nextTime_s = getMsByDate(item5_time, 1);
                                    nextTime_e = getMsByDate(item5_time, 2);

                                    // 반복 종료가 없으면
                                    if (repeatTimes==0){
                                        repeatTimes = REPEATAMOUNT;
                                    }

                                    for (int i=0;i<repeatTimes-1;i++){
                                        nextTime_s += (86400000 * 7 * repeatPeriod);
                                        nextTime_e += (86400000 * 7 * repeatPeriod);
                                        dbHelper.todoDataInsert(item1_title, item2_loc, item3_isDynamic, item4_isAllDay, getNextTimeByMs(nextTime_s, nextTime_e), item6_category, item7_Memo, item8_needTime, item9_repeatId);
                                    }

                                    Toast.makeText(getApplicationContext(), "반복 일정이 등록되었습니다", Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;

                                // 매월 반복
                                case 4:

                                    dbHelper.updateRepeatId();
                                    item9_repeatId = dbHelper.getCurrentRepeatId();
                                    dbHelper.todoDataInsert(item1_title, item2_loc, item3_isDynamic, item4_isAllDay, item5_time, item6_category, item7_Memo, item8_needTime, item9_repeatId);

                                    String nextMonth = item5_time;

                                    // 반복 종료가 없으면
                                    if (repeatTimes==0){
                                        repeatTimes = REPEATAMOUNT;
                                    }
                                    for (int i=0;i<repeatTimes-1;i++){
                                        nextMonth = getNextMonth(nextMonth);
                                        dbHelper.todoDataInsert(item1_title, item2_loc, item3_isDynamic, item4_isAllDay, nextMonth, item6_category, item7_Memo, item8_needTime, item9_repeatId);
                                    }

                                    Toast.makeText(getApplicationContext(), "반복 일정이 등록되었습니다", Toast.LENGTH_SHORT).show();
                                    finish();

                                    break;

                                // 매년 반복
                                case 5:

                                    dbHelper.updateRepeatId();
                                    item9_repeatId = dbHelper.getCurrentRepeatId();
                                    dbHelper.todoDataInsert(item1_title, item2_loc, item3_isDynamic, item4_isAllDay, item5_time, item6_category, item7_Memo, item8_needTime, item9_repeatId);

                                    String nextYear = item5_time;

                                    // 반복 종료가 없으면
                                    if (repeatTimes==0){
                                        repeatTimes = REPEATAMOUNT;
                                    }
                                    for (int i=0;i<repeatTimes-1;i++){
                                        nextYear = getNextYear(nextYear);
                                        dbHelper.todoDataInsert(item1_title, item2_loc, item3_isDynamic, item4_isAllDay, nextYear, item6_category, item7_Memo, item8_needTime, item9_repeatId);
                                    }

                                    Toast.makeText(getApplicationContext(), "반복 일정이 등록되었습니다", Toast.LENGTH_SHORT).show();
                                    finish();



                                    break;
                                default:
                                    break;
                            }
                        }else if (modeAddEdit == EDIT_MODE){
                            Toast.makeText(getApplicationContext(), "일정이 수정되었습니다", Toast.LENGTH_SHORT).show();
                            dbHelper.todoDataUpdate(item0_id, item1_title, item2_loc, item3_isDynamic, item4_isAllDay, item5_time, item6_category, item7_Memo, item8_needTime, item9_repeatId);
                            finish();
                        }
                    }
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
                    intentCategory.putExtra("categoryMode", tvCategory.getText().toString());
                    startActivityForResult(intentCategory, REQUEST_CATEGORY);
                    break;
                case R.id.linFooterView:

                    // 반복 아닌 일정 삭제
                    if (itemElement.mRepeatId==0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
                        builder.setTitle("일정 삭제")
                                .setMessage("일정을 삭제하시겠습니까?")
                                .setPositiveButton("삭제",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dbHelper.deleteTodoDataById(item0_id);
                                                Toast.makeText(getApplicationContext(), "일정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                .setNegativeButton("취소",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                        AlertDialog dialog = builder.create();    // 알림창 객체 생성
                        dialog.show();
                    }
                    // 반복 일정 삭제
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
                        builder.setTitle("일정 삭제")
                                .setMessage("반복된 일정을 삭제하시겠습니까?")
                                .setPositiveButton("취소",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                .setNeutralButton("모든 일정에도 적용",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dbHelper.deleteTodoDataByRepeatId(itemElement.mId, itemElement.mRepeatId, itemElement.mTime);
                                                Toast.makeText(getApplicationContext(), "모든 반복 일정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                .setNegativeButton("이 일정에만 적용",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dbHelper.deleteTodoDataById(itemElement.mId);
                                                Toast.makeText(getApplicationContext(), "일정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                        AlertDialog dialog = builder.create();    // 알림창 객체 생성
                        dialog.show();
                    }

                    break;

                default:
                    break;
            }
        }
    };

}
