package com.pkteam.smartcalendar.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pkteam.smartcalendar.R;

/*
 * Created by paeng on 2018. 7. 11..
 */

public class AddItemActivityCategory extends AppCompatActivity {
    private LinearLayout llCategory1, llCategory2, llCategory3, llCategory4, llCategory5, llCancel;
    private ImageView ivCategory1, ivCategory2, ivCategory3, ivCategory4, ivCategory5;
    private TextView tvCategory1, tvCategory2, tvCategory3, tvCategory4, tvCategory5;
    private Button btnSubmit;

    private int categoryMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem_category);
        bindingView();
        Intent intent = getIntent();
        setImageView(getCategoryInteger(intent.getStringExtra("categoryMode")));
    }

    private int getCategoryInteger(String inputString){
        if(inputString.equals(tvCategory1.getText().toString())){
            categoryMode=1;
        }else if (inputString.equals(tvCategory2.getText().toString())){
            categoryMode=2;
        }else if (inputString.equals(tvCategory3.getText().toString())){
            categoryMode=3;
        }else if (inputString.equals(tvCategory4.getText().toString())){
            categoryMode=4;
        }else if (inputString.equals(tvCategory5.getText().toString())){
            categoryMode=5;
        }else {
            Toast.makeText(AddItemActivityCategory.this, "error occured in category", Toast.LENGTH_SHORT).show();
        }
        return categoryMode;
    }
    private String getCategoryString(int mode){
        String returnString = "";
        switch (mode){
            case 1:
                returnString = tvCategory1.getText().toString();
                break;
            case 2:
                returnString = tvCategory2.getText().toString();
                break;
            case 3:
                returnString = tvCategory3.getText().toString();
                break;
            case 4:
                returnString = tvCategory4.getText().toString();
                break;
            case 5:
                returnString = tvCategory5.getText().toString();
                break;
            default:
                Toast.makeText(AddItemActivityCategory.this, "error occured in category", Toast.LENGTH_SHORT).show();
                break;
        }
        return returnString;
    }


    private void bindingView(){
        llCategory1 = findViewById(R.id.ll_category_1);
        llCategory2 = findViewById(R.id.ll_category_2);
        llCategory3 = findViewById(R.id.ll_category_3);
        llCategory4 = findViewById(R.id.ll_category_4);
        llCategory5 = findViewById(R.id.ll_category_5);
        llCategory1.setOnClickListener(listener);
        llCategory2.setOnClickListener(listener);
        llCategory3.setOnClickListener(listener);
        llCategory4.setOnClickListener(listener);
        llCategory5.setOnClickListener(listener);

        ivCategory1 = findViewById(R.id.iv_category_1);
        ivCategory2 = findViewById(R.id.iv_category_2);
        ivCategory3 = findViewById(R.id.iv_category_3);
        ivCategory4 = findViewById(R.id.iv_category_4);
        ivCategory5 = findViewById(R.id.iv_category_5);

        tvCategory1 = findViewById(R.id.tv_category_1);
        tvCategory2 = findViewById(R.id.tv_category_2);
        tvCategory3 = findViewById(R.id.tv_category_3);
        tvCategory4 = findViewById(R.id.tv_category_4);
        tvCategory5 = findViewById(R.id.tv_category_5);

        btnSubmit = findViewById(R.id.btn_submit);
        llCancel = findViewById(R.id.ll_cancel);
        btnSubmit.setOnClickListener(listener);
        llCancel.setOnClickListener(listener);



    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_category_1:
                    categoryMode = 1;
                    setImageView(categoryMode);
                    break;
                case R.id.ll_category_2:
                    categoryMode = 2;
                    setImageView(categoryMode);
                    break;
                case R.id.ll_category_3:
                    categoryMode = 3;
                    setImageView(categoryMode);
                    break;
                case R.id.ll_category_4:
                    categoryMode = 4;
                    setImageView(categoryMode);
                    break;
                case R.id.ll_category_5:
                    categoryMode = 5;
                    setImageView(categoryMode);
                    break;
                case R.id.ll_cancel:
                    finish();
                    break;
                case R.id.btn_submit:
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("categoryInteger", categoryMode);
                    returnIntent.putExtra("categoryResult",getCategoryString(categoryMode));
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    break;
            }
        }
    };
    private void setImageView(int ver){
        switch (ver){
            case 1:
                ivCategory1.setVisibility(View.VISIBLE);
                ivCategory2.setVisibility(View.INVISIBLE);
                ivCategory3.setVisibility(View.INVISIBLE);
                ivCategory4.setVisibility(View.INVISIBLE);
                ivCategory5.setVisibility(View.INVISIBLE);
                break;
            case 2:
                ivCategory1.setVisibility(View.INVISIBLE);
                ivCategory2.setVisibility(View.VISIBLE);
                ivCategory3.setVisibility(View.INVISIBLE);
                ivCategory4.setVisibility(View.INVISIBLE);
                ivCategory5.setVisibility(View.INVISIBLE);
                break;
            case 3:
                ivCategory1.setVisibility(View.INVISIBLE);
                ivCategory2.setVisibility(View.INVISIBLE);
                ivCategory3.setVisibility(View.VISIBLE);
                ivCategory4.setVisibility(View.INVISIBLE);
                ivCategory5.setVisibility(View.INVISIBLE);
                break;
            case 4:
                ivCategory1.setVisibility(View.INVISIBLE);
                ivCategory2.setVisibility(View.INVISIBLE);
                ivCategory3.setVisibility(View.INVISIBLE);
                ivCategory4.setVisibility(View.VISIBLE);
                ivCategory5.setVisibility(View.INVISIBLE);
                break;
            case 5:
                ivCategory1.setVisibility(View.INVISIBLE);
                ivCategory2.setVisibility(View.INVISIBLE);
                ivCategory3.setVisibility(View.INVISIBLE);
                ivCategory4.setVisibility(View.INVISIBLE);
                ivCategory5.setVisibility(View.VISIBLE);
                break;

        }
    }


}
