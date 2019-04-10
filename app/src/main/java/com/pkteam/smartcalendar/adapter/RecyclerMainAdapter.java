package com.pkteam.smartcalendar.adapter;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkteam.smartcalendar.GetTimeInformation;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.model.MyData;
import com.pkteam.smartcalendar.view.AddItemActivity;

import java.util.ArrayList;

class MyViewHolderHeader extends RecyclerView.ViewHolder{

    TextView itemHeaderText;

    MyViewHolderHeader(View itemView) {
        super(itemView);
        itemHeaderText = itemView.findViewById(R.id.item_header_text);
    }
}

class MyViewHolderStatic extends RecyclerView.ViewHolder{

    ImageView itemCategory;
    TextView itemTitle, itemTime;
    LinearLayout itemParent;

    MyViewHolderStatic(View itemView) {
        super(itemView);
        itemCategory = itemView.findViewById(R.id.item_category);
        itemTitle = itemView.findViewById(R.id.item_text);
        itemTime = itemView.findViewById(R.id.item_time);
        itemParent = itemView.findViewById(R.id.item_parent);
    }
}

class MyViewHolderDynamic extends RecyclerView.ViewHolder{

    ImageView itemCategory;
    TextView itemTitle, itemDeadline;
    LinearLayout itemParent;

    MyViewHolderDynamic(View itemView) {
        super(itemView);
        itemCategory = itemView.findViewById(R.id.item_category);
        itemTitle = itemView.findViewById(R.id.item_text);
        itemDeadline = itemView.findViewById(R.id.item_time);
        itemParent = itemView.findViewById(R.id.item_parent);
    }
}



public class RecyclerMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MyData> mDataSet = new ArrayList<>();
    private Context mContext;

    public RecyclerMainAdapter(Context context, ArrayList<MyData> searchDataSet) {
        this.mDataSet = searchDataSet;
        this.mContext = context;
    }


    @Override
    public int getItemViewType(int position) {
        if(mDataSet.get(position).getMode()==0)
            return 0;
        else if(mDataSet.get(position).getMode() == 1)
            return 1;
        else if(mDataSet.get(position).getMode() == 2)
            return 2;
        else if (mDataSet.get(position).getMode() == 3)
            return 3;
        else if (mDataSet.get(position).getMode() == 10)
            return 10;
        else if (mDataSet.get(position).getMode() == 11)
            return 11;
        else if (mDataSet.get(position).getMode() == 12)
            return 12;
        else if (mDataSet.get(position).getMode() == 13)
            return 13;

        else
            return 13;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        if(viewType==0) // Header Type
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.layout_listitem_header, parent, false);
            return new MyViewHolderHeader(view);
        }
        else if (viewType == 1) // Static Type
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.layout_listitem, parent, false);
            return new MyViewHolderStatic(view);
        }
        else if (viewType == 2) // Dynamic Type
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.layout_listitem, parent, false);
            return new MyViewHolderDynamic(view);
        }
        else if (viewType == 3) // explain Type
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.layout_listitem_explain, parent, false);
            return new MyViewHolderHeader(view);
        }
        else if (viewType == 10)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.layout_listitem_calendar_header, parent, false);
            return new MyViewHolderHeader(view);
        }
        else if (viewType == 11)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.layout_listitem_calendar, parent, false);
            return new MyViewHolderStatic(view);
        }
        else if (viewType == 12)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.layout_listitem_calendar, parent, false);
            return new MyViewHolderDynamic(view);
        }
        else if (viewType == 13)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.layout_listitem_calendar_explain, parent, false);
            return new MyViewHolderHeader(view);
        }
        else
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.layout_listitem_header, parent, false);
            return new MyViewHolderHeader(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(holder.getItemViewType()) {

            case 0:
            case 10:
            {
                MyViewHolderHeader viewHolder = (MyViewHolderHeader) holder;
                viewHolder.setIsRecyclable(false);
                viewHolder.itemHeaderText.setText(this.mDataSet.get(position).mHeader);
            }
            break;

            case 1:
            case 11:
            {
                final MyData selectedData = this.mDataSet.get(position);
                MyViewHolderStatic viewHolder = (MyViewHolderStatic) holder;
                viewHolder.itemCategory.setImageDrawable(getCategoryDrawable(selectedData.mCategory));
                viewHolder.itemTitle.setText(selectedData.mTitle);
                viewHolder.itemTime.setText(getShowingTime(selectedData.mTime.split("\\."), selectedData.mNeedTime, 1));
                viewHolder.setIsRecyclable(false);

                viewHolder.itemParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(view.getContext(), AddItemActivity.class);
                        intent.putExtra("mode",2);
                        intent.putExtra("id", selectedData.mId);
                        view.getContext().startActivity(intent);
                    }
                });
            }
            break;
            case 2:
            case 12:
            {
                final MyData selectedData = this.mDataSet.get(position);
                MyViewHolderDynamic viewHolder = (MyViewHolderDynamic) holder;
                viewHolder.itemCategory.setImageDrawable(getCategoryDrawable(selectedData.mCategory));
                viewHolder.itemTitle.setText(selectedData.mTitle);
                viewHolder.itemDeadline.setText(getShowingTime(selectedData.mTime.split("\\."), selectedData.mNeedTime, 2));
                viewHolder.setIsRecyclable(false);

                viewHolder.itemParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(view.getContext(), AddItemActivity.class);
                        intent.putExtra("mode",2);
                        intent.putExtra("id", selectedData.mId);
                        view.getContext().startActivity(intent);
                    }
                });
            }
            break;

            case 3:
            case 13:
            {
                MyViewHolderHeader viewHolder = (MyViewHolderHeader) holder;
                viewHolder.setIsRecyclable(false);
                viewHolder.itemHeaderText.setText(this.mDataSet.get(position).mHeader);
            }
            break;

            default:

            break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
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
        else{
            return "error";
        }
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
