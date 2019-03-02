package com.pkteam.smartcalendar.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.ActivityAddItemCategoryBinding;

/*
 * Created by paeng on 2018. 7. 11..
 */

public class AddItemActivityCategory extends AppCompatActivity {

    private int categoryMode;
    ActivityAddItemCategoryBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_item_category);
        binding.setAdditem(this);

        bindingView();
        Intent intent = getIntent();
        setImageView(getCategoryInteger(intent.getStringExtra("categoryMode")));
    }

    private int getCategoryInteger(String inputString){
        if(inputString.equals(binding.tvCategory1.getText().toString())){
            categoryMode=1;
        }else if (inputString.equals(binding.tvCategory2.getText().toString())){
            categoryMode=2;
        }else if (inputString.equals(binding.tvCategory3.getText().toString())){
            categoryMode=3;
        }else if (inputString.equals(binding.tvCategory4.getText().toString())){
            categoryMode=4;
        }else if (inputString.equals(binding.tvCategory5.getText().toString())){
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
                returnString = binding.tvCategory1.getText().toString();
                break;
            case 2:
                returnString = binding.tvCategory2.getText().toString();
                break;
            case 3:
                returnString = binding.tvCategory3.getText().toString();
                break;
            case 4:
                returnString = binding.tvCategory4.getText().toString();
                break;
            case 5:
                returnString = binding.tvCategory5.getText().toString();
                break;
            default:
                Toast.makeText(AddItemActivityCategory.this, "error occured in category", Toast.LENGTH_SHORT).show();
                break;
        }
        return returnString;
    }


    private void bindingView(){
        binding.llCategory1.setOnClickListener(listener);
        binding.llCategory2.setOnClickListener(listener);
        binding.llCategory3.setOnClickListener(listener);
        binding.llCategory4.setOnClickListener(listener);
        binding.llCategory5.setOnClickListener(listener);

        DBHelper dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);

        binding.tvCategory1.setText(dbHelper.getAllCategory().get(0));
        binding.tvCategory2.setText(dbHelper.getAllCategory().get(1));
        binding.tvCategory3.setText(dbHelper.getAllCategory().get(2));
        binding.tvCategory4.setText(dbHelper.getAllCategory().get(3));
        binding.tvCategory5.setText(dbHelper.getAllCategory().get(4));

        binding.btnSubmit.setOnClickListener(listener);

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
                case R.id.btn_submit:
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("categoryInteger", categoryMode);
                    returnIntent.putExtra("categoryResult", getCategoryString(categoryMode));
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    break;
            }
        }
    };
    private void setImageView(int ver){
        switch (ver){
            case 1:
                binding.ivCategory1.setVisibility(View.VISIBLE);
                binding.ivCategory2.setVisibility(View.INVISIBLE);
                binding.ivCategory3.setVisibility(View.INVISIBLE);
                binding.ivCategory4.setVisibility(View.INVISIBLE);
                binding.ivCategory5.setVisibility(View.INVISIBLE);
                break;
            case 2:
                binding.ivCategory1.setVisibility(View.INVISIBLE);
                binding.ivCategory2.setVisibility(View.VISIBLE);
                binding.ivCategory3.setVisibility(View.INVISIBLE);
                binding.ivCategory4.setVisibility(View.INVISIBLE);
                binding.ivCategory5.setVisibility(View.INVISIBLE);
                break;
            case 3:
                binding.ivCategory1.setVisibility(View.INVISIBLE);
                binding.ivCategory2.setVisibility(View.INVISIBLE);
                binding.ivCategory3.setVisibility(View.VISIBLE);
                binding.ivCategory4.setVisibility(View.INVISIBLE);
                binding.ivCategory5.setVisibility(View.INVISIBLE);
                break;
            case 4:
                binding.ivCategory1.setVisibility(View.INVISIBLE);
                binding.ivCategory2.setVisibility(View.INVISIBLE);
                binding.ivCategory3.setVisibility(View.INVISIBLE);
                binding.ivCategory4.setVisibility(View.VISIBLE);
                binding.ivCategory5.setVisibility(View.INVISIBLE);
                break;
            case 5:
                binding.ivCategory1.setVisibility(View.INVISIBLE);
                binding.ivCategory2.setVisibility(View.INVISIBLE);
                binding.ivCategory3.setVisibility(View.INVISIBLE);
                binding.ivCategory4.setVisibility(View.INVISIBLE);
                binding.ivCategory5.setVisibility(View.VISIBLE);
                break;

        }
    }

    public void finishView(View view){
        finish();
    }

}
