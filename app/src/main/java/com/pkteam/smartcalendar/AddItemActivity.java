package com.pkteam.smartcalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    // Date and Time picker
    LinearLayout llTimeStart, llTimeEnd;
    TextView tvTimeStart, tvTimeEnd;
    SingleDateAndTimePickerDialog.Builder singleBuilder;
    SimpleDateFormat simpleTimeFormat;
    String[] dateString;



    Boolean isEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        bindingView();
        initSet();
        setupConcealerNSV();
    }

    private void bindingView(){
        // Header, Footer
        concealerNSV = findViewById(R.id.concealerNSV);
        crdHeaderView = findViewById(R.id.crdHeaderView);
        linFooterView = findViewById(R.id.linFooterView);
        // 날짜 시간 선택
        llTimeStart = findViewById(R.id.ll_time_start);
        llTimeEnd = findViewById(R.id.ll_time_end);
        tvTimeStart = findViewById(R.id.tv_time_start);
        tvTimeEnd = findViewById(R.id.tv_time_end);
        llTimeStart.setOnClickListener(listener);
        llTimeEnd.setOnClickListener(listener);

    }
    public void initSet(){
        this.simpleTimeFormat = new SimpleDateFormat("YYYY.MM.dd/HH:mm", Locale.getDefault());

        isEdit = true;

        if(isEdit){
            linFooterView.setVisibility(View.GONE);
        }
        String date[] = getCurrentDate().split("/");
        tvTimeStart.setText(date[0] + "    " + date[1]);
        tvTimeEnd.setText(date[0] + "    " + date[1]);
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

            singleBuilder.listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {
                    Log.d("CheckPaeng", simpleTimeFormat.format(date));
                    //YYYY.MM.dd/HH:mm
                    dateString = simpleTimeFormat.format(date).split("/");

                    tvTimeStart.setText(dateString[0] + "    " + dateString[1]);

                }
            });
        }else if(ver==2){
            singleBuilder.listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {
                    Log.d("CheckPaeng", simpleTimeFormat.format(date));
                    //YYYY.MM.dd/HH:mm
                    dateString = simpleTimeFormat.format(date).split("/");

                    tvTimeEnd.setText(dateString[0] + "    " + dateString[1]);

                }
            });
        }
        singleBuilder.display();

    }

    View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_time_start:
                    selectDateAndTime(1);

                    break;
                case R.id.ll_time_end:
                    selectDateAndTime(2);
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
