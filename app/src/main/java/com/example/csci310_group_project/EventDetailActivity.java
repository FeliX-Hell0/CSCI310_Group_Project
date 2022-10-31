package com.example.csci310_group_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetailActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_event_detail_layout);

        // init
        context = this;

        // parse event index from extra

        int eventIdx = -1;

        ImageView imgView = findViewById(R.id.custom_event_info_img_view);
        TextView dateText = findViewById(R.id.custom_event_date);
        TextView titleText = findViewById(R.id.custom_event_title);
        TextView organizerText = findViewById(R.id.custom_event_organizer);
        TextView descriptionText = findViewById(R.id.custom_event_description);
        TextView costText = findViewById(R.id.custom_event_cost);
        Button favButton = findViewById(R.id.custom_event_favorite_button);
        Button regButton = findViewById(R.id.register_button);

        // check if in favorite list

        // check if in already registered
        // display text accordingly


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventIdx = Integer.valueOf(extras.getString("EVENT_INDEX"));

            // TODO: call service to get event info via index
            titleText.setText("The event index is " + String.valueOf(eventIdx));

            // TODO: update display accordingly
        }
    }
}