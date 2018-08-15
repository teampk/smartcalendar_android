package com.pkteam.smartcalendar.view.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.view.DataCheckActivity;
import com.pkteam.smartcalendar.view.SettingCategory;
import com.simmorsal.library.ConcealerNestedScrollView;

/*
 * Created by paeng on 2018. 7. 4..
 */

public class FragmentSetting extends Fragment {

    private LinearLayout llCategorySetting, llDeleteAllData, llCheckData, llAppInformation;
    private CardView crdHeaderView;
    private ConcealerNestedScrollView concealerNSV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_setting, container, false);
        bindingView(mView);

        return mView;
    }
    private void bindingView(View mView){

        llCategorySetting = mView.findViewById(R.id.ll_category_setting);
        llCategorySetting.setOnClickListener(listener);
        llDeleteAllData = mView.findViewById(R.id.ll_delete_all_data);
        llDeleteAllData.setOnClickListener(listener);
        llCheckData = mView.findViewById(R.id.ll_check_data);
        llCheckData.setOnClickListener(listener);
        llAppInformation = mView.findViewById(R.id.ll_app_information);
        llAppInformation.setOnClickListener(listener);
        crdHeaderView = mView.findViewById(R.id.crdHeaderView);

        concealerNSV = mView.findViewById(R.id.concealerNSV);

        setupConcealerNSV();
    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_delete_all_data:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("모든 데이터를 삭제합니다");
                    builder.setMessage("추가한 모든 일정 데이터를 삭제합니다. 계속하시겠습니까?");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DBHelper dbHelper = new DBHelper(getContext(), "SmartCal.db", null, 1);
                                    dbHelper.deleteTodoDataAll();
                                    dbHelper.initializeRepeatId();
                                    Toast.makeText(getContext(), "모든 데이터가 삭제되었습니다", Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();

                    break;
                case R.id.ll_check_data:
                    Intent mIntent = new Intent(getContext(), DataCheckActivity.class);
                    startActivity(mIntent);
                    break;
                case R.id.ll_category_setting:
                    Intent intent = new Intent(getContext(), SettingCategory.class);
                    startActivity(intent);
                    break;
                case R.id.ll_app_information:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("");

                    break;
                default:
                    break;
            }
        }
    };

    private void setupConcealerNSV() {
        // if you're setting header and footer views at the very start of the layout creation (onCreate),
        // as the views are not drawn yet, the library cant get their correct sizes, so you have to do this:

        crdHeaderView.post(new Runnable() {
            @Override
            public void run() {
                concealerNSV.setHeaderView(crdHeaderView, 0);
            }
        });


        // pass a true to setHeaderFastHide to make views hide faster
        concealerNSV.setHeaderFastHide(true);
    }
}
