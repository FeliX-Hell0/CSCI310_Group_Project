package com.example.csci310_group_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csci310_group_project.ui.login.LoginActivity;

import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity {
    private static final int Event_COUNT = 20;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        // init
        context = this;

        // create cells via Inflate
        GridLayout layout = findViewById(R.id.grid_layout_event_list);

        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i < Event_COUNT; i++) {
            GridLayout eventBox = (GridLayout) li.inflate(R.layout.custom_event_box_layout, layout, false);

            Button button = eventBox.findViewById(R.id.custom_event_detail_button);
            int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.i("event clicked", "button click");

                    // sent intent to event detail page
                    Intent intent = new Intent(ExploreActivity.this, EventDetailActivity.class);
//                    intent.putExtra("event_index", finalI);
                    startActivity(intent);
                }
            });


            GridLayout.LayoutParams lp = (GridLayout.LayoutParams) eventBox.getLayoutParams();
            lp.rowSpec = GridLayout.spec(i);
            lp.columnSpec = GridLayout.spec(0);
            layout.addView(eventBox, lp);
        }
    }

//    public void onClickShowEventDetail(View view) {
//
//        // inflate the layout of the popup window
//        LayoutInflater inflater = (LayoutInflater)
//                getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupView = inflater.inflate(R.layout.custom_event_box_layout, null);
//
//        // create the popup window
//        int width = GridLayout.LayoutParams.WRAP_CONTENT;
//        int height = GridLayout.LayoutParams.WRAP_CONTENT;
//        boolean focusable = true; // lets taps outside the popup also dismiss it
//        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//        // show the popup window
//        // which view you pass in doesn't matter, it is only used for the window tolken
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//        // dismiss the popup window when touched
//        popupView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                popupWindow.dismiss();
//                return true;
//            }
//        });
//    }
}