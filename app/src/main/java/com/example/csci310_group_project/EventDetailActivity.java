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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csci310_group_project.fragment.ExploreFragment;
import com.example.csci310_group_project.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    private Context context;
    private String user = "";
    private String eventName = "";
    private boolean regStatus = false;
    private boolean favStatus = false;

    private String startTime = "";
    private int duration = 0;

    private long eventStart = 0;
    private long eventEnd = 0;
    private boolean conflicted = false;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_event_detail_layout);

        // init
        context = this;
        db = FirebaseFirestore.getInstance();

        ImageView imgView = findViewById(R.id.custom_event_info_img_view);
        TextView dateText = findViewById(R.id.custom_event_date);
        TextView titleText = findViewById(R.id.custom_event_title);
        TextView locationText = findViewById(R.id.custom_event_location);
        TextView organizerText = findViewById(R.id.custom_event_organizer);
        TextView descriptionText = findViewById(R.id.custom_event_description);
        TextView typeText = findViewById(R.id.custom_event_type);
        TextView costText = findViewById(R.id.custom_event_cost);
        Button favButton = findViewById(R.id.custom_event_favorite_button);
        Button regButton = findViewById(R.id.custom_event_register_button);

        TextView durationText = findViewById(R.id.custom_event_duration);

        // parse event name from extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getIntentExtras(extras);

            titleText.setText(eventName);
            if(favStatus){
                favButton.setText("Remove from favorites");
            }
            if(regStatus){
                regButton.setText("Unregister");
            }

            // TODO: call db to get event info event name
            DocumentReference docRef = db.collection("allEvent").document(eventName);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot !=null){

                            Log.d("MapIntent", documentSnapshot.getString("name"));
                            Event event = new Event(documentSnapshot.getString("name"), documentSnapshot.getString("type"),
                                    documentSnapshot.getString("date"), documentSnapshot.getString("sponsoring_org"), documentSnapshot.getString("description"),
                                    documentSnapshot.getString("location"), Math.toIntExact(documentSnapshot.getLong("cost")),0, false, false, 0.0, 0.0);

                            startTime = documentSnapshot.getString("date");
                            duration = Math.toIntExact(documentSnapshot.getLong("duration"));
                            // TODO: update display accordingly
                            dateText.setText(event.getEventDate());
                            typeText.setText(event.getEventType());
                            locationText.setText(event.getEventLocation());
                            organizerText.setText(event.getEventOrganizor());
                            descriptionText.setText(event.getEventDescription());

                            Integer durationMinute = Math.toIntExact(documentSnapshot.getLong("duration"));
                            // convert durationMinute to hour and minute
                            Integer numHours = durationMinute / 60;
                            Integer numMins = durationMinute % 60;
                            durationText.setText("Duration: " + String.valueOf(numHours) + " hr " + String.valueOf(numMins) + " min");

                            costText.setText("Cost: $" + String.valueOf(event.getEventCost()));
                        }
                    }
                }
            });
        }

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!regStatus) {
                    registerEvents();
                } else{
                    unRegisterEvents();
                }
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!favStatus) {
                    favEvents();
                } else{
                    unFavEvents();
                }
            }
        });
    }

    private void getIntentExtras(Bundle extras) {
        eventName = extras.getString("event_name");
        user = extras.getString("user");
        favStatus = extras.getBoolean("favorite");
        regStatus = extras.getBoolean("register");
    }


    private void favEvents(){
        if(user.equals("")){
            Toast.makeText(this, "Please register to add events to favorites", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            return;
        }

        DocumentReference docRef = db.collection("users").document(user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Toast.makeText(getActivity(), "Please wait", Toast.LENGTH_LONG).show();
                        String collection = document.getString("favorites") + ";" + eventName;
                        Log.d("FavCollection", collection);
                        addFavEvent(collection);
                    }
                } else {
                    Toast.makeText(EventDetailActivity.this, "Connection error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerEvents(){
        if(user.equals("")){
            Toast.makeText(this, "Please register to sign up for events", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            return;
        }

        DocumentReference docRef = db.collection("users").document(user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // TODO: check reg time conflict
                        String registeredEvents = document.getString("registeredEvents");
                        String timeFrame = document.getString("time");


                        updateEvent(registeredEvents + ";" + eventName, timeFrame + ";" + String.valueOf(eventStart) + "," + String.valueOf(eventEnd));

                    } else {
                        //Toast.makeText(EventDetailActivity.this, "No user registration info", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EventDetailActivity.this, "Connection error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void conflict(String timeFrame){


        String[] timeCollection = timeFrame.split(";");
        Log.d("hour", timeFrame);

        String month = startTime.split("/")[0];
        String day = startTime.split("/")[1];
        String year = startTime.split("/")[2].split(",")[0];
        String hrs = startTime.split("/")[2].split(",")[1].split(":")[0].replaceAll("\\s+","");
        String min = startTime.split("/")[2].split(",")[1].split(":")[1];

        long start = Integer.parseInt(min) + Integer.parseInt(hrs)*60 + Integer.parseInt(day)*3600 + Integer.parseInt(month)*108000 + (Integer.parseInt(year) - 2000)*1296000;
        long end = start + duration;

        Log.d("startTime", String.valueOf(start) + " " + String.valueOf(end));

        if(timeFrame.equals("")){
            conflicted = false;
            return;
        }

        for(String s : timeCollection){
            long ss = Long.parseLong(s.split(",")[0]);
            long ee = Long.parseLong(s.split(",")[1]);

            if((start >= ss && start <= ee)||(end >= ss && end <= ee)){
                conflicted = true;
                return;
            }
        }

        //conflicted
    }

    private void unRegisterEvents(){
        // check if user logged in, not sure if needed
        if(user.equals("")){
            Toast.makeText(this, "Please register to sign up for events", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            return;
        }

        DocumentReference docRef = db.collection("users").document(user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String temp = document.getString("registeredEvents");
                        if(temp.contains(eventName)){
                            int firstIdx = temp.indexOf(eventName) - 1;
                            int lastIdx = temp.lastIndexOf(eventName);
                            String collection1 = "";
                            if (firstIdx > 0){
                                collection1 = temp.substring(0,firstIdx);
                            }
                            String collection2 = "";
                            if (lastIdx < temp.length() - 1) {
                                collection2 = temp.substring(lastIdx + 1);
                            }
                            //updateEvent(collection1 + collection2);
                        }
                    } else {
                        //Toast.makeText(EventDetailActivity.this, "No user registration info", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(EventDetailActivity.this, "Connection error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void unFavEvents(){
        // check if user logged in, not sure if needed
        if(user.equals("")){
            Toast.makeText(this, "Please register to add events to favorites", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            return;
        }

        DocumentReference docRef = db.collection("users").document(user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String temp = document.getString("favorites");
                        if(temp.contains(eventName)){
                            int firstIdx = temp.indexOf(eventName) - 1;
                            int lastIdx = temp.lastIndexOf(eventName);
                            String collection1 = "";
                            if (firstIdx > 0){
                                collection1 = temp.substring(0,firstIdx);
                            }
                            String collection2 = "";
                            if (lastIdx < temp.length() - 1) {
                                collection2 = temp.substring(lastIdx + 1);
                            }
                            addFavEvent(collection1 + collection2);
                        }
                    } else {
                        //Toast.makeText(EventDetailActivity.this, "No user registration info", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(EventDetailActivity.this, "Connection error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateEvent(String collection, String timeUpdate){
        db.collection("users").document(user).update("registeredEvents", collection).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                db.collection("users").document(user).update("time", timeUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("startTime2", timeUpdate);
                        Intent intent = new Intent(EventDetailActivity.this, ContentActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                });

            }
        });
    }

    private void addFavEvent(String collection){
        db.collection("users").document(user).update("favorites", collection).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(EventDetailActivity.this, ContentActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("toFav", "true");
                startActivity(intent);
            }
        });
    }
}