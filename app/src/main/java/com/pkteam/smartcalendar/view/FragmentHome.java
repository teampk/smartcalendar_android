package com.pkteam.smartcalendar.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;

import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.pkteam.smartcalendar.ArrayListSorting;
import com.pkteam.smartcalendar.adapter.RecyclerMainAdapter;
import com.pkteam.smartcalendar.databinding.FragmentHomeBinding;
import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.model.MyData;
import com.pkteam.smartcalendar.R;
import com.singh.daman.gentletoast.GentleToast;

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
    private boolean show=false;

    FragmentHomeBinding binding;

    ArrayList<MyData> mDataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        final View mView = binding.getRoot();
        binding.tvTime.setText(getCurrentDate());
        initRecyclerView(mView);
        initSpeedDial(savedInstanceState == null, mView);


        binding.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show)
                    hideComponents(mView); // if the animation is shown, we hide back the views
                else
                    showComponents(mView); // if the animation is NOT shown, we animate the views
            }
        });

        binding.tvTime.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                GentleToast.with(getContext()).longToast(sdf.format(date)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_sc).show();
                return true;
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

    private void initDataset(){
        dbHelper = new DBHelper(getContext(), "SmartCal.db", null, 1);
        staticData = new ArrayList<>();
        dynamicData = new ArrayList<>();
        staticData = dbHelper.getTodoStaticData();
        dynamicData = dbHelper.getTodoDynamicData();
    }

    private void initRecyclerView(View mView){

        binding.recyclerTotal.setHasFixedSize(true);
        binding.recyclerTotal.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerTotal.scrollToPosition(0);

        mDataList.clear();
        mDataList.add(new MyData(getString(R.string.string_static), 0));

        // no data in static
        if(arrayListSorting.sortingForStaticForToday(staticData).size() == 0){
            mDataList.add(new MyData(getString(R.string.string_no_data_static), 3));
        }else{
            mDataList.addAll(arrayListSorting.sortingForStaticForToday(staticData));
        }

        mDataList.add(new MyData(getString(R.string.string_dynamic), 0));

        // no data in dynamic
        if(arrayListSorting.sortingForDynamicFromNow(dynamicData).size() == 0){
            mDataList.add(new MyData(getString(R.string.string_no_data_dynamic), 3));
        }else{
            mDataList.addAll(arrayListSorting.sortingForDynamicFromNow(dynamicData));
        }

        mDataList.add(new MyData("", 0));


        RecyclerMainAdapter mainAdapter = new RecyclerMainAdapter(mView.getContext(), mDataList);

        binding.recyclerTotal.setAdapter(mainAdapter);
        binding.recyclerTotal.setItemAnimator(new DefaultItemAnimator());

    }

    private void showComponents(View mView){
        show = true;

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(getContext(), R.layout.fragment_home_detail);

        binding.tvTime.setText(getString(R.string.top_bar_detail));


        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1200);
        ConstraintLayout constraint = mView.findViewById(R.id.constraint);
        TransitionManager.beginDelayedTransition(constraint, transition);
        constraintSet.applyTo(constraint);
        //here constraint is the name of view to which we are applying the constraintSet
    }

    private void hideComponents(View mView){
        show = false;

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(getContext(), R.layout.fragment_home);

        binding.tvTime.setText(getCurrentDate());

        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1200);
        ConstraintLayout constraint = mView.findViewById(R.id.constraint);

        TransitionManager.beginDelayedTransition(constraint, transition);
        constraintSet.applyTo(constraint); //here constraint is the name of view to which we are applying the constraintSet
    }

    public String getCurrentDate(){
        // get Current Date and Time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String[] mDate = sdf.format(date).split("/");
        return mDate[0]+"년 "+mDate[1]+"월 "+mDate[2]+"일";
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

}
