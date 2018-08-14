package com.pkteam.smartcalendar.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;

import java.util.ArrayList;

/*
 * Created by paeng on 2018. 8. 14..
 */

public class SettingCategory extends AppCompatActivity{

    private LinearLayout llCancel;
    private Button btnSubmit;
    private EditText et1, et2, et3, et4, et5;
    private ArrayList<String> categoryList;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting_category);

        bindingView();
        loadData();
    }
    private void bindingView(){
        et1 = findViewById(R.id.et_1);
        et2 = findViewById(R.id.et_2);
        et3 = findViewById(R.id.et_3);
        et4 = findViewById(R.id.et_4);
        et5 = findViewById(R.id.et_5);
        llCancel = findViewById(R.id.ll_cancel);
        btnSubmit = findViewById(R.id.btn_submit);

        llCancel.setOnClickListener(listener);
        btnSubmit.setOnClickListener(listener);
    }

    private void loadData(){
        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);
        categoryList = dbHelper.getAllCategory();
        et1.setText(categoryList.get(0));
        et2.setText(categoryList.get(1));
        et3.setText(categoryList.get(2));
        et4.setText(categoryList.get(3));
        et5.setText(categoryList.get(4));

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_cancel:
                    finish();
                    break;
                case R.id.btn_submit:
                    dbHelper.categoryUpdate(et1.getText().toString(), et2.getText().toString(),
                            et3.getText().toString(), et4.getText().toString(), et5.getText().toString());
                    Toast.makeText(SettingCategory.this, "카테고리가 수정되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                    break;

                default:
                    break;
            }
        }
    };
}
