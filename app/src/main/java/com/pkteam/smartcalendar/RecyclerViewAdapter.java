package com.pkteam.smartcalendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pkteam.smartcalendar.model.MyData;
import com.pkteam.smartcalendar.view.AddItemActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
 * Created by paeng on 2018. 7. 5..
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<MyData> mDataSet = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<MyData> searchDataSet){
        this.mDataSet = searchDataSet;
        this.mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.itemCategory.setImageDrawable(getCategoryDrawable(this.mDataSet.get(position).mCategory));
        holder.itemTitle.setText(this.mDataSet.get(position).mTitle);
        if (this.mDataSet.get(position).mIsDynamic){
            holder.itemTime.setText(getShowingTimeDynamic(this.mDataSet.get(position).mTime.split("\\.")));
        }else{
            holder.itemTime.setText(getShowingTimeStatic(this.mDataSet.get(position).mTime.split("\\.")));
        }

        holder.itemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddItemActivity.class);
                intent.putExtra("mode",2);
                intent.putExtra("id", mDataSet.get(position).mId);
                view.getContext().startActivity(intent);
            }
        });
    }
    private String getShowingTimeDynamic(String[] input){
        Calendar tday = Calendar.getInstance();
        Calendar dday = Calendar.getInstance();
        //20180725

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String[] mDate = sdf.format(date).split("/");
        tday.set(Integer.valueOf(mDate[0]),
                Integer.valueOf(mDate[1]),
                Integer.valueOf(mDate[2]));

        dday.set(Integer.valueOf(input[2].substring(0,4)),
                Integer.valueOf(input[2].substring(4,6)),
                Integer.valueOf(input[2].substring(6,8)));

        int count = (int)((tday.getTimeInMillis()/86400000) - (dday.getTimeInMillis()/86400000));
        String output;

        if (count == 0){
            output = "D-"+String.valueOf(count);
        }else {
            output = "D"+String.valueOf(count);
        }
        return output;
    }
    @NonNull
    private String getShowingTimeStatic(String[] input){
        //201807082130
        String startTime, endTime;

        if(Long.parseLong(input[0].substring(0,8))<Long.parseLong(getCurrentDate())){
           startTime = "0000";
        }else{
            startTime = input[0].substring(8,12);
        }

        if(Long.parseLong(getCurrentDate()) < Long.parseLong(input[1].substring(0,8))){
            endTime = "2400";
        }else{
            endTime = input[1].substring(8,12);
        }

        return startTime.substring(0,2)+":"+startTime.substring(2,4)+"~"+endTime.substring(0,2)+":"+endTime.substring(2,4);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemCategory;
        TextView itemTitle, itemTime;
        RelativeLayout itemParent;

        public ViewHolder(View itemView){
            super(itemView);
            itemParent = itemView.findViewById(R.id.item_parent);

            itemCategory = itemView.findViewById(R.id.item_category);
            itemTitle = itemView.findViewById(R.id.item_text);
            itemTime = itemView.findViewById(R.id.item_time);
        }
    }

    private String getCurrentDate(){
        // get Current Date and Time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String[] mDate = sdf.format(date).split("/");
        return mDate[0]+mDate[1]+mDate[2];
    }

    private Drawable getCategoryDrawable(int category){
        Drawable categoryDrawable = null;
        switch (category){
            case 1:
                categoryDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_category_1_24dp);
                break;
            case 2:
                categoryDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_category_2_24dp);
                break;
            case 3:
                categoryDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_category_3_24dp);
                break;
            case 4:
                categoryDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_category_4_24dp);
                break;
            case 5:
                categoryDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_category_5_24dp);
                break;

        }


        return categoryDrawable;

    }
}
