package com.pkteam.smartcalendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkteam.smartcalendar.model.MyData;
import com.pkteam.smartcalendar.view.AddItemActivity;

import java.util.ArrayList;

/*
 * Created by paeng on 2018. 7. 5..
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<MyData> mDataSet = new ArrayList<>();
    private ArrayList<MyData> mainDataStatic;
    private ArrayList<MyData> mainDataDynamic;

    private Context mContext;
    private int mode;
    private long date;

    public ArrayList<Integer> getSelectedId() {
        return selectedId;
    }
    private ArrayList<Integer> selectedId;

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemCategory;
        TextView itemTitle, itemTime;
        RelativeLayout itemParent;

        ViewHolder(View itemView){
            super(itemView);
            itemParent = itemView.findViewById(R.id.item_parent);

            itemCategory = itemView.findViewById(R.id.item_category);
            itemTitle = itemView.findViewById(R.id.item_text);
            itemTime = itemView.findViewById(R.id.item_time);
        }
    }

    public RecyclerViewAdapter(Context context, ArrayList<MyData> searchDataSet, int mode){
        this.mDataSet = searchDataSet;
        this.mContext = context;
        this.mode = mode;
        this.date = 0;
    }
    public RecyclerViewAdapter(Context context, ArrayList<MyData> searchDataSet, int mode, long date){
        this.mDataSet = searchDataSet;
        this.mContext = context;
        this.mode = mode;
        this.date = date;
    }
    // For Main
    public RecyclerViewAdapter(Context context, ArrayList<MyData> dataStatic, ArrayList<MyData> dataDynamic, int mode){
        this.mContext = context;
        this.mainDataStatic = dataStatic;
        this.mainDataDynamic = dataDynamic;
        this.mode = mode;
        this.date = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (mode==1 || mode==2){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        }else if (mode==3 || mode==4){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_calendar, parent, false);
        }else if (mode==5 || mode==6){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_scheduling, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final MyData selectedData = this.mDataSet.get(position);

        holder.itemCategory.setImageDrawable(getCategoryDrawable(selectedData.mCategory));
        holder.itemTitle.setText(selectedData.mTitle);
        holder.itemTime.setText(getShowingTime(selectedData.mTime.split("\\."), selectedData.mNeedTime, mode));
        selectedData.selected = false;

        selectedId = new ArrayList<>();

        if (mode==1 || mode==2 || mode==3 || mode==4){
            holder.itemParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(view.getContext(), AddItemActivity.class);
                    intent.putExtra("mode",2);
                    intent.putExtra("id", selectedData.mId);
                    view.getContext().startActivity(intent);
                }
            });
        }else{
            holder.itemParent.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(!selectedData.isSelected()){
                        holder.itemParent.setBackgroundResource(R.color.material_gray_300);
                        selectedData.selected = true;
                        selectedId.add(selectedData.mId);
                    }else{
                        holder.itemParent.setBackgroundResource(R.color.material_white_1000);
                        selectedData.selected = false;
                        for (int i=0; i<getSelectedId().size();i++){
                            if(getSelectedId().get(i) == selectedData.mId){
                                selectedId.remove(i);
                            }
                        }
                    }

                }
            });
        }


    }
    private String getShowingTime(String[] input, int needTime, int mode){

        GetTimeInformation timeInformation = new GetTimeInformation();


        // home Static
        if (mode == 1){
            //201807082130
            String startTime, endTime;

            if(Long.parseLong(input[0].substring(0,8))<Long.parseLong(timeInformation.getCurrentDate())){
                startTime = "0000";
            }else{
                startTime = input[0].substring(8,12);
            }

            if(Long.parseLong(timeInformation.getCurrentDate()) < Long.parseLong(input[1].substring(0,8))){
                endTime = "2400";
            }else{
                endTime = input[1].substring(8,12);
            }

            return startTime.substring(0,2)+":"+startTime.substring(2,4)+"~"+endTime.substring(0,2)+":"+endTime.substring(2,4);
        }
        // D-day가 나오게 (Dynamic)
        else if (mode == 2 || mode == 4){
            return timeInformation.getDday(input[2]);
        }
        // calendar static
        else if (mode == 3){
            //201807082130
            String startTime, endTime;

            if(Long.parseLong(input[0].substring(0,8))<Long.parseLong(timeInformation.getCurrentDate(this.date))){
                startTime = "0000";
            }else{
                startTime = input[0].substring(8,12);
            }

            if(Long.parseLong(timeInformation.getCurrentDate(this.date)) < Long.parseLong(input[1].substring(0,8))){
                endTime = "2400";
            }else{
                endTime = input[1].substring(8,12);
            }

            return startTime.substring(0,2)+":"+startTime.substring(2,4)+"~"+endTime.substring(0,2)+":"+endTime.substring(2,4);
        }
        // scheduling dynamic
        else if (mode == 5){
            return "데드라인 : " + input[2].substring(0,4)+"년 "+input[2].substring(4,6)+"월 "+input[2].substring(6,8)+"일 "+input[2].substring(8,10)+":"
                    +input[2].substring(10)+"  (" +timeInformation.getDday(input[2])+")\n필요시간 : " + needTime + " 시간";
        }
        else{
            return "error";
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
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
