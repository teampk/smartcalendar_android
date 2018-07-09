package com.pkteam.smartcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import com.simmorsal.library.ConcealerNestedScrollView;


/*
 * Created by paeng on 2018. 7. 5..
 */

public class AddItemActivity extends AppCompatActivity {

    ConcealerNestedScrollView concealerNSV;
    CardView crdHeaderView;
    LinearLayout linFooterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        concealerNSV = findViewById(R.id.concealerNSV);
        crdHeaderView = findViewById(R.id.crdHeaderView);
        linFooterView = findViewById(R.id.linFooterView);

        setupConcealerNSV();
    }

    private void setupConcealerNSV() {
        // if you're setting header and footer views at the very start of the layout creation (onCreate),
        // as the views are not drawn yet, the library cant get their correct sizes, so you have to do this:

        crdHeaderView.post(new Runnable() {
            @Override
            public void run() {
                concealerNSV.setHeaderView(crdHeaderView, 0);
            }
        });
        linFooterView.post(new Runnable() {
            @Override
            public void run() {
                concealerNSV.setFooterView(linFooterView, 0);
            }
        });


        // pass a true to setHeaderFastHide to make views hide faster
        concealerNSV.setHeaderFastHide(true);
    }
}
