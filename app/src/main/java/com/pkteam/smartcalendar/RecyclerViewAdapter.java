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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        holder.itemTime.setText(this.mDataSet.get(position).mTime);


        holder.itemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), AddItemActivity.class);
                view.getContext().startActivity(intent);

            }
        });
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
    private int getCategoryColor(int category){
        int categoryColor=0;
        switch (category){
            case 1:
                categoryColor = mContext.getResources().getColor(R.color.colorCategory1);
                break;
            case 2:
                categoryColor = mContext.getResources().getColor(R.color.colorCategory2);
                break;
            case 3:
                categoryColor = mContext.getResources().getColor(R.color.colorCategory3);
                break;
            case 4:
                categoryColor = mContext.getResources().getColor(R.color.colorCategory4);
                break;
            case 5:
                categoryColor = mContext.getResources().getColor(R.color.colorCategory5);
                break;

        }
        return categoryColor;

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
