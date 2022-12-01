package com.example.csci310_group_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
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
    private GestureDetector mDetector;

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
        TextView popularityText = findViewById(R.id.custom_event_popularity);
        Button favButton = findViewById(R.id.custom_event_favorite_button);
        Button regButton = findViewById(R.id.custom_event_register_button);

        TextView durationText = findViewById(R.id.custom_event_duration);

        // parse event name from extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getIntentExtras(extras);
            String imageName = eventName;
            if (eventName.equals("Abbot Kidding: A Comedy Show in Venice") ){
                imageName = "Abbot Kidding";
            }else if (eventName.equals("The Setup Presents: Citizen Public Market Comedy Night")){
                imageName = "Citizen Public Market Comedy Night";
            }else if (eventName.equals("Joachim Horsley: Caribbean Nocturnes In Concert")){
                imageName = "Caribbean Nocturnes In Concert";
            }else if (eventName.equals("MoreLuv: RnB")){
                imageName = "MoreLuv & RnB";
            }
            StorageReference mSotrage = FirebaseStorage.getInstance().getReference("eventImage").child(imageName+".png");
            try {
                final File local = File.createTempFile("event","png");
                mSotrage.getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(local.getAbsolutePath());
                        imgView.setImageBitmap(bitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            titleText.setText(eventName);
            if(favStatus){
                favButton.setText("Remove from favorites");
            }
            if(regStatus){
                regButton.setText("Unregister");
            }

            // call db to get event info event name
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
                                    documentSnapshot.getString("location"), Math.toIntExact(documentSnapshot.getLong("cost")),0, false, false, 0.0, 0.0, (int) (long) (documentSnapshot.getLong("registered")));

                            startTime = documentSnapshot.getString("date");
                            duration = Math.toIntExact(documentSnapshot.getLong("duration"));

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
                            popularityText.setText(String.valueOf(event.getEventPopularity()) + " registered");
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

        View myView = findViewById(R.id.detail_whole_view);
        mDetector = new GestureDetector(this, new MyGestureListener());

        // Add a touch listener to the view
        // The touch listener passes all its events on to the gesture detector
        myView.setOnTouchListener(touchListener);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG","onDown: ");

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("TAG", "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("TAG", "onDoubleTap: ");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i("TAG", "onScroll: ");

            Intent intent = new Intent(EventDetailActivity.this, ContentActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("toFav", "true");
            startActivity(intent);

            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("TAG", "onFling: ");
            return true;
        }
    }
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // pass the events to the gesture detector
            // a return value of true means the detector is handling it
            // a return value of false means the detector didn't
            // recognize the event
            return mDetector.onTouchEvent(event);

        }
    };

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
                        conflict(timeFrame);
                        Log.d("startTime3", String.valueOf(eventStart));

                        if(!conflicted) {
                            updateEvent(registeredEvents + ";" + eventName, timeFrame + ";" + String.valueOf(eventStart) + "," + String.valueOf(eventEnd));
                        }
                        else{

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            //Yes button clicked
                                            updateEvent(registeredEvents + ";" + eventName, timeFrame + ";" + String.valueOf(eventStart) + "," + String.valueOf(eventEnd));
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            Intent i = new Intent(EventDetailActivity.this, ContentActivity.class);
                                            i.putExtra("user", user);
                                            startActivity(i);
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Time conflict! Continue registration?").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();

                            //Toast.makeText(EventDetailActivity.this, "Time conflict!", Toast.LENGTH_LONG).show();
                        }

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

        eventStart = start;
        eventEnd = end;

        if(timeFrame.equals("")){
            conflicted = false;
            return;
        }

        for(String s : timeCollection){
            Log.d("StartTime5", s);
            if(s.equals("")){
                continue;
            }
            long ss = Long.parseLong(s.split(",")[0]);
            long ee = Long.parseLong(s.split(",")[1]);


            if((start >= ss && start <= ee)||(end >= ss && end <= ee)){
                conflicted = true;
                Log.d("StartTim4", "Conflict!");
                return;
            }
        }

        conflicted = false;
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
                        String month = startTime.split("/")[0];
                        String day = startTime.split("/")[1];
                        String year = startTime.split("/")[2].split(",")[0];
                        String hrs = startTime.split("/")[2].split(",")[1].split(":")[0].replaceAll("\\s+","");
                        String min = startTime.split("/")[2].split(",")[1].split(":")[1];

                        long start = Integer.parseInt(min) + Integer.parseInt(hrs)*60 + Integer.parseInt(day)*3600 + Integer.parseInt(month)*108000 + (Integer.parseInt(year) - 2000)*1296000;
                        long end = start + duration;

                        String temp2 = String.valueOf(start) + "," + String.valueOf(end);
                        String userTime = document.getString("time");

                        Log.d("deleteTime1", temp2 + " " + userTime);

                        if(temp.contains(eventName) && userTime.contains(temp2)){
                            int firstIdx = temp.indexOf(eventName) - 1;
                            int lastIdx = firstIdx+eventName.length();
                            String collection1 = "";
                            if (firstIdx > 0){
                                collection1 = temp.substring(0,firstIdx);
                            }
                            String collection2 = "";
                            if (lastIdx < temp.length() - 1) {
                                collection2 = temp.substring(lastIdx + 1);
                            }

                            int fir = userTime.indexOf(temp2)-1;
                            int las = fir+temp2.length();
                            String col1 = "";
                            if(fir > 0){
                                col1 = userTime.substring(0, fir);
                            }
                            String col2 = "";
                            if(las < userTime.length()-1){
                                col2 = userTime.substring(las+1);
                            }
                            Log.d("deleteTime", String.valueOf(fir) + " " + String.valueOf(las));
                            updateEvent(collection1 + collection2, col1+col2);
                        }

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
                            int lastIdx = firstIdx + eventName.length();
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
                        Log.d("startTime2", collection);
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