package com.pkteam.smartcalendar;

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

    ConcealerNestedScrollView concealerNSV;
    CardView crdHeaderView;
    LinearLayout linFooterView;

    Button btnCancel, btnSubmit;

    EditText etTitle, etLoc, etMemo;
    TextView tvStaticDynamic;
    Switch swStaticDynamic;

    // Date and Time picker
    LinearLayout llTimeStart, llTimeEnd, llTimeDeadline, llStatic, llDynamic;
    TextView tvTimeStart, tvTimeEnd, tvTimeDeadline;
    SingleDateAndTimePickerDialog.Builder singleBuilder;
    SimpleDateFormat simpleTimeFormat;
    String[] dateString;

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
        llTimeStart = findViewById(R.id.ll_time_start);
        llTimeEnd = findViewById(R.id.ll_time_end);
        llTimeDeadline = findViewById(R.id.ll_time_deadline);
        tvTimeStart = findViewById(R.id.tv_time_start);
        tvTimeEnd = findViewById(R.id.tv_time_end);
        tvTimeDeadline = findViewById(R.id.tv_time_deadline);
        llTimeStart.setOnClickListener(listener);
        llTimeEnd.setOnClickListener(listener);
        llTimeDeadline.setOnClickListener(listener);
        this.simpleTimeFormat = new SimpleDateFormat("YYYY.MM.dd/HH:mm", Locale.getDefault());



    }
    public void initSet(boolean isEdit){
        // 수정하는 경우
        if(isEdit){
            linFooterView.setVisibility(View.VISIBLE);

        }
        // 추가하는 경우
        else{
            linFooterView.setVisibility(View.GONE);
            String date[] = getCurrentDate().split("/");
            tvTimeStart.setText(date[0] + "    " + date[1]);
            tvTimeEnd.setText(date[0] + "    " + date[1]);
            tvTimeDeadline.setText(date[0] + "    "+ date[1]);


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
    }
    // for Switch

    public void setSwitchToDynamic(){
        llStatic.setVisibility(View.GONE);
        llDynamic.setVisibility(View.VISIBLE);
        tvStaticDynamic.setText("Dynamic");

    }

    public void setSwitchToStatic(){
        llStatic.setVisibility(View.VISIBLE);
        llDynamic.setVisibility(View.GONE);
        tvStaticDynamic.setText("Static");
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
                .defaultDate(defaultDate)

                //.titleTextColor(Color.GREEN)
                //.backgroundColor(Color.BLACK)
                //.mainColor(Color.GREEN)
                .displayMinutes(true)
                .displayHours(true)
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
                    tvTimeStart.setText(dateString[0] + "    " + dateString[1]);

                }
            });
        }else if(ver==2){
            singleBuilder.listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {
                    dateString = simpleTimeFormat.format(date).split("/");
                    tvTimeEnd.setText(dateString[0] + "    " + dateString[1]);

                }
            });
        }else if(ver==3){
            singleBuilder.listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {
                    dateString = simpleTimeFormat.format(date).split("/");
                    tvTimeDeadline.setText(dateString[0] + "    " + dateString[1]);

                }
            });
        }
        singleBuilder.display();

    }

    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_cancel:
                    finish();
                    break;
                case R.id.btn_add:
                    Toast.makeText(getApplicationContext(), "등록할까 말까", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ll_time_start:
                    selectDateAndTime(1);
                    break;
                case R.id.ll_time_end:
                    selectDateAndTime(2);
                    break;
                case R.id.ll_time_deadline:
                    selectDateAndTime(3);
                    break;

            }
        }
    };

    public String getCurrentDate(){
        // get Current Date and Time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd/HH:mm");
        return sdf.format(date);
    }
}
