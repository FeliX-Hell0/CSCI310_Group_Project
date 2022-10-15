package com.example.csci310_group_project;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity {
    private static final int Event_COUNT = 20;

    private Context context;
//    private ArrayList<TextView> cell_tvs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        // init
        context = this;

        // create cells via Inflate
        GridLayout layout = findViewById(R.id.gridLayoutEventList);

        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i < Event_COUNT; i++) {
            GridLayout tv = (GridLayout) li.inflate(R.layout.custom_event_box_layout, layout, false);
//
//            // TODO: event handler
//
            GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
            lp.rowSpec = GridLayout.spec(i);
            lp.columnSpec = GridLayout.spec(0);
//
            layout.addView(tv, lp);
        }
    }
}