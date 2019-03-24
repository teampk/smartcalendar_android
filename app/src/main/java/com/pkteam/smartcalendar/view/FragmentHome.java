package com.pkteam.smartcalendar.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.pkteam.smartcalendar.ArrayListSorting;
import com.pkteam.smartcalendar.databinding.FragmentHomeBinding;
import com.pkteam.smartcalendar.view.AddItemActivity;
import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.model.MyData;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.RecyclerViewAdapter;
import com.pkteam.smartcalendar.view.ScheduleItemActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * Created by paeng on 2018. 7. 4..
 */

public class FragmentHome extends Fragment {
    private ArrayList<MyData> staticData, dynamicData;
    private ArrayListSorting arrayListSorting = new ArrayListSorting();
    private DBHelper dbHelper;

    FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View mView = binding.getRoot();
        binding.tvTime.setText(getCurrentDate());
        initRecyclerView(mView);
        initSpeedDial(savedInstanceState == null, mView);

        binding.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Toast.makeText(getContext(), sdf.format(date), Toast.LENGTH_SHORT).show();
            }
        });


        return mView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }

    @Override
    public void onResume() {
        super.onResume();
        View mView = binding.getRoot();
        binding.tvTime.setText(getCurrentDate());
        initDataset();
        initRecyclerView(mView);
    }

    private void initSpeedDial(boolean addActionItems, View mView) {

        if (addActionItems) {
            binding.speedDial.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_add_item, R.drawable.ic_add_white_24dp)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getContext().getTheme()))
                    .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000, getContext().getTheme()))
                    .setLabel(getString(R.string.label_add_item))
                    .setLabelColor(Color.BLACK)
                    .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000, getContext().getTheme()))
                    .create());

            binding.speedDial.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_auto_scheduling, R.drawable.ic_dashboard_white_24dp)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getContext().getTheme()))
                    .setLabel(getString(R.string.label_scheduling))
                    .setLabelColor(Color.BLACK)
                    .setLabelBackgroundColor(Color.WHITE)
                    .setTheme(R.style.AppTheme_Purple)
                    .create());
            binding.speedDial.getUseReverseAnimationOnClose();

        }

        //Set main action clicklistener.
        binding.speedDial.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {

                return false; // True to keep the Speed Dial open
            }

            @Override
            public void onToggleChanged(boolean isOpen) {
                // 열리면 true
                // 닫히면 false
            }
        });

        //Set option fabs clicklisteners.
        binding.speedDial.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_add_item:
                        Intent intent = new Intent(getContext(), AddItemActivity.class);
                        intent.putExtra("mode", 1);
                        startActivity(intent);
                        return false; // closes without animation (same as mSpeedDialView.close(false); return false;)
                    case R.id.fab_auto_scheduling:
                        Intent intentScheduling = new Intent(getContext(), ScheduleItemActivity.class);
                        startActivity(intentScheduling);
                        return false;
                    default:
                        break;
                }
                return true; // To keep the Speed Dial open
            }
        });

    }




    public String getCurrentDate(){
        // get Current Date and Time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String[] mDate = sdf.format(date).split("/");
        return mDate[0]+"년 "+mDate[1]+"월 "+mDate[2]+"일";
    }


    private void initDataset(){
        dbHelper = new DBHelper(getContext(), "SmartCal.db", null, 1);
        staticData = new ArrayList<>();
        dynamicData = new ArrayList<>();
        staticData = dbHelper.getTodoStaticData();
        dynamicData = dbHelper.getTodoDynamicData();
    }

    private void initRecyclerView(View mView){
        // static
        binding.recycler1.setHasFixedSize(true);
        binding.recycler1.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recycler1.scrollToPosition(0);
        binding.recycler1.setAdapter(new RecyclerViewAdapter(mView.getContext(), arrayListSorting.sortingForStaticForToday(staticData),1));
        binding.recycler1.setItemAnimator(new DefaultItemAnimator());

        // dynamic
        binding.recycler2.setHasFixedSize(true);
        binding.recycler2.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recycler2.scrollToPosition(0);
        binding.recycler2.setAdapter(new RecyclerViewAdapter(mView.getContext(), arrayListSorting.sortingForDynamicFromNow(dynamicData),2));
        binding.recycler2.setItemAnimator(new DefaultItemAnimator());
    }

}
