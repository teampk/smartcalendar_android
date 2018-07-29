package com.pkteam.smartcalendar.view.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.view.DataCheckActivity;

/*
 * Created by paeng on 2018. 7. 4..
 */

public class FragmentSetting extends Fragment {

    private Button btnReset, btnDataCheck;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_setting, container, false);
        bindingView(mView);

        return mView;
    }
    private void bindingView(View mView){
        btnReset = mView.findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(listener);
        btnDataCheck = mView.findViewById(R.id.btn_data_check);
        btnDataCheck.setOnClickListener(listener);
    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_reset:
                    DBHelper dbHelper = new DBHelper(getContext(), "SmartCal.db", null, 1);
                    dbHelper.deleteTodoDataAll();
                    Toast.makeText(getContext(), "Delete All the Data", Toast.LENGTH_LONG).show();
                    break;
                case R.id.btn_data_check:
                    Intent mIntent = new Intent(getContext(), DataCheckActivity.class);
                    startActivity(mIntent);
                    break;
                default:
                    break;
            }
        }
    };
}
