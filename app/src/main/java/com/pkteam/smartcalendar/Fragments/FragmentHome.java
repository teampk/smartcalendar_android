package com.pkteam.smartcalendar.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.content.res.AppCompatResources;
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
import com.pkteam.smartcalendar.AddItemActivity;
import com.pkteam.smartcalendar.MyData;
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
    private RecyclerView mRecyclerViewStatic, mRecyclerViewDynamic;
    private RecyclerView.LayoutManager mLayoutManagerStatic, mLayoutManagerDynamic;
    private RecyclerViewAdapter mAdapterStatic, mAdapterDynamic;
    private TextView tvTime;
    private Toast mToast;
    private Snackbar mSnackbar;

    private SpeedDialView mSpeedDialView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_home, container, false);
        bindingView(mView);
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
                        startActivity(intent);
                        return false; // closes without animation (same as mSpeedDialView.close(false); return false;)
                    case R.id.fab_auto_scheduling:
                        showToast("Coming Soon...");
                        return false;
                    default:
                        break;
                }
                return true; // To keep the Speed Dial open
            }
        });

    }
    /*
    @Override
    public void onBackPressed() {
        //Closes menu if its opened.
        if (mSpeedDialView.isOpen()) {
            mSpeedDialView.close();
        } else {
            super.onBackPressed();
        }

    }
    */

    public void bindingView(View mView){

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
        staticData = new ArrayList<>();
        dynamicData = new ArrayList<>();

        staticData.add(new MyData(1, "찬일이랑 점심", "12:00~13:00"));
        staticData.add(new MyData(2, "찬일이랑 코노", "13:00~14:00"));
        staticData.add(new MyData(3, "찬일이랑 카페", "14:00~15:00"));
        staticData.add(new MyData(2, "찬일이랑 배그", "15:00~17:00"));
        staticData.add(new MyData(1, "찬일이랑 헬스", "17:00~19:00"));
        staticData.add(new MyData(1, "찬일이랑 저녁", "19:00~20:00"));
        staticData.add(new MyData(4, "찬일이랑 스타트업 회의", "20:00~22:00"));

        dynamicData.add(new MyData(4, "유튜브 업로드", "D-1"));
        dynamicData.add(new MyData(3, "클로버 인공지능 아이디어 서류 제출", "D-3"));
        dynamicData.add(new MyData(3, "스마트캘린더 뼈대 코딩", "D-5"));
        dynamicData.add(new MyData(3, "Always-On 중간 제출", "D-19"));
        dynamicData.add(new MyData(1, "PET COMM 사업계획서 제출", "D-24"));

    }

    private void initRecyclerView(View mView){
        mLayoutManagerStatic = new LinearLayoutManager(getActivity());
        mRecyclerViewStatic = mView.findViewById(R.id.recycler_1);
        mRecyclerViewStatic.setHasFixedSize(true);
        mRecyclerViewStatic.setLayoutManager(mLayoutManagerStatic);
        mRecyclerViewStatic.scrollToPosition(0);
        mAdapterStatic = new RecyclerViewAdapter(mView.getContext(), staticData);
        mRecyclerViewStatic.setAdapter(mAdapterStatic);
        mRecyclerViewStatic.setItemAnimator(new DefaultItemAnimator());


        mLayoutManagerDynamic = new LinearLayoutManager(getActivity());
        mRecyclerViewDynamic = mView.findViewById(R.id.recycler_2);
        mRecyclerViewDynamic.setHasFixedSize(true);
        mRecyclerViewDynamic.setLayoutManager(mLayoutManagerDynamic);
        mRecyclerViewDynamic.scrollToPosition(0);
        mAdapterDynamic = new RecyclerViewAdapter(mView.getContext(), dynamicData);
        mRecyclerViewDynamic.setAdapter(mAdapterDynamic);
        mRecyclerViewDynamic.setItemAnimator(new DefaultItemAnimator());
    }

    protected void showToast(String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
        mToast.show();
    }

    protected void showSnackbar(View mView, String text) {
        mSnackbar = Snackbar.make(mView, text, Snackbar.LENGTH_SHORT);
        mSnackbar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSnackbar.dismiss();
            }
        });
        mSnackbar.show();
    }
}
