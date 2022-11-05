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

import com.example.csci310_group_project.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    private Context context;
    private String user = "";
    private String eventName = "";
    private boolean regStatus = false;
    private boolean favStatus = false;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_event_detail_layout);

        // init
        context = this;
        db = FirebaseFirestore.getInstance();

        // parse event index from extra

        String eventIdx = "";

        ImageView imgView = findViewById(R.id.custom_event_info_img_view);
        TextView dateText = findViewById(R.id.custom_event_date);
        TextView titleText = findViewById(R.id.custom_event_title);
        TextView organizerText = findViewById(R.id.custom_event_organizer);
        TextView descriptionText = findViewById(R.id.custom_event_description);
        TextView costText = findViewById(R.id.custom_event_cost);
        Button favButton = findViewById(R.id.custom_event_favorite_button);
        Button regButton = findViewById(R.id.custom_event_register_button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventName = extras.getString("event_name");
            user = extras.getString("user");

            // TODO: call service to get event info via index


            // TODO: update display accordingly
            titleText.setText(eventName);
            favStatus = extras.getBoolean("favorite");
            regStatus = extras.getBoolean("register");
            if(favStatus){
                favButton.setText("Remove from favorites");
            }
            if(regStatus){
                regButton.setText("Unregister");
            }
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
                        String collection = document.getString("registeredEvents") + ";" + eventName;
                        updateEvent(collection);
                    } else {
                        //Toast.makeText(EventDetailActivity.this, "No user registration info", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EventDetailActivity.this, "Connection error", Toast.LENGTH_LONG).show();
                }
            }
        });
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
                            updateEvent(collection1 + collection2);
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

    private void updateEvent(String collection){
        db.collection("users").document(user).update("registeredEvents", collection).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(EventDetailActivity.this, ContentActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
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