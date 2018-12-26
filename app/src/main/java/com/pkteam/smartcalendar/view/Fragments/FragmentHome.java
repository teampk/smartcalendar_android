package com.pkteam.smartcalendar.view.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.pkteam.smartcalendar.view.AddItemActivity;
import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.model.MyData;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.RecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * Created by paeng on 2018. 7. 4..
 */

public class FragmentHome extends Fragment {
    private ArrayList<MyData> staticData, dynamicData;
    private ArrayList<MyData> staticDataSorted, dynamicDataSorted;
    private RecyclerView mRecyclerViewStatic, mRecyclerViewDynamic;
    private RecyclerView.LayoutManager mLayoutManagerStatic, mLayoutManagerDynamic;
    private RecyclerViewAdapter mAdapterStatic, mAdapterDynamic;
    private TextView tvTime;
    private Toast mToast;
    private Snackbar mSnackbar;
    private SpeedDialView mSpeedDialView;
    private View mView;
    private ArrayListSorting arrayListSorting = new ArrayListSorting();
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        bindingView();
        initSpeedDial(savedInstanceState == null, mView);
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
        initDataset();
        initRecyclerView(mView);
    }

    private void initSpeedDial(boolean addActionItems, View mView) {
        mSpeedDialView = mView.findViewById(R.id.speedDial);

        if (addActionItems) {

            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_add_item, R.drawable.ic_add_white_24dp)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, getContext().getTheme()))
                    .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000, getContext().getTheme()))
                    .setLabel(getString(R.string.label_add_item))
                    .setLabelColor(Color.BLACK)
                    .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000, getContext().getTheme()))
                    .create());

            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_auto_scheduling, R.drawable.ic_dashboard_white_24dp)
                    .setLabel(getString(R.string.label_scheduling))
                    .setLabelColor(Color.BLACK)
                    .setLabelBackgroundColor(Color.WHITE)
                    .setTheme(R.style.AppTheme_Purple)
                    .create());
            mSpeedDialView.getUseReverseAnimationOnClose();

        }

        //Set main action clicklistener.
        mSpeedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
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
        mSpeedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_add_item:
                        Intent intent = new Intent(getContext(), AddItemActivity.class);
                        intent.putExtra("mode", 1);
                        startActivity(intent);
                        return false; // closes without animation (same as mSpeedDialView.close(false); return false;)
                    case R.id.fab_auto_scheduling:
                        Toast.makeText(getContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
                        dbHelper.updateRepeatId();

                        return false;
                    default:
                        break;
                }
                return true; // To keep the Speed Dial open
            }
        });

    }

    public void bindingView(){

        tvTime = mView.findViewById(R.id.tv_time);
        tvTime.setText(getCurrentDate());
        initRecyclerView(mView);

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
        mLayoutManagerStatic = new LinearLayoutManager(getActivity());
        mRecyclerViewStatic = mView.findViewById(R.id.recycler_1);
        mRecyclerViewStatic.setHasFixedSize(true);
        mRecyclerViewStatic.setLayoutManager(mLayoutManagerStatic);
        mRecyclerViewStatic.scrollToPosition(0);
        mAdapterStatic = new RecyclerViewAdapter(mView.getContext(), arrayListSorting.sortingForStaticForToday(staticData),1);
        mRecyclerViewStatic.setAdapter(mAdapterStatic);
        mRecyclerViewStatic.setItemAnimator(new DefaultItemAnimator());


        mLayoutManagerDynamic = new LinearLayoutManager(getActivity());
        mRecyclerViewDynamic = mView.findViewById(R.id.recycler_2);
        mRecyclerViewDynamic.setHasFixedSize(true);
        mRecyclerViewDynamic.setLayoutManager(mLayoutManagerDynamic);
        mRecyclerViewDynamic.scrollToPosition(0);
        mAdapterDynamic = new RecyclerViewAdapter(mView.getContext(), arrayListSorting.sortingForDynamicFromToday(dynamicData),1);
        mRecyclerViewDynamic.setAdapter(mAdapterDynamic);
        mRecyclerViewDynamic.setItemAnimator(new DefaultItemAnimator());
    }

}
