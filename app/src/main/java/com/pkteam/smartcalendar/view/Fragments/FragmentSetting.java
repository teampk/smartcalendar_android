package com.pkteam.smartcalendar.view.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.FragmentSettingBinding;
import com.pkteam.smartcalendar.view.ViewSetting.DataCheckActivity;
import com.pkteam.smartcalendar.view.ViewSetting.SettingCategory;
import com.pkteam.smartcalendar.view.ViewSetting.SettingSleepTime;
import com.simmorsal.library.ConcealerNestedScrollView;

/*
 * Created by paeng on 2018. 7. 4..
 */

public class FragmentSetting extends Fragment {

    FragmentSettingBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        View mView = binding.getRoot();
        bindingView(mView);
        return mView;
    }

    private void bindingView(View mView){
        binding.llCategorySetting.setOnClickListener(listener);
        binding.llDeleteAllData.setOnClickListener(listener);
        binding.llCheckData.setOnClickListener(listener);
        binding.llAppInformation.setOnClickListener(listener);
        binding.llSleepTime.setOnClickListener(listener);

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
                    Toast.makeText(getContext(), "Smart Calendar 1.0.0", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.ll_sleep_time:
                    Intent intents = new Intent(getContext(), SettingSleepTime.class);
                    startActivity(intents);


                    break;
                default:
                    break;
            }
        }
    };

    private void setupConcealerNSV() {
        // if you're setting header and footer views at the very start of the layout creation (onCreate),
        // as the views are not drawn yet, the library cant get their correct sizes, so you have to do this:

        binding.crdHeaderView.post(new Runnable() {
            @Override
            public void run() {
                binding.concealerNSV.setHeaderView(binding.crdHeaderView, 0);
            }
        });

        // pass a true to setHeaderFastHide to make views hide faster
        binding.concealerNSV.setHeaderFastHide(true);
    }
}
