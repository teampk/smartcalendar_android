package com.pkteam.smartcalendar.model;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecyclerData {
    private String itemTitle, itemCategory, itemTime;

    private String itemHeaderText;
    private int mode;


    public RecyclerData(String itemTitle, String itemCategory, String itemTime, int mode) {
        this.itemTitle = itemTitle;
        this.itemCategory = itemCategory;
        this.itemTime = itemTime;
        this.mode = mode;
    }


    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemTime() {
        return itemTime;
    }

    public void setItemTime(String itemTime) {
        this.itemTime = itemTime;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getItemHeaderText() {
        return itemHeaderText;
    }

    public void setItemHeaderText(String itemHeaderText) {
        this.itemHeaderText = itemHeaderText;
    }
}
