package com.example.csci310_group_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csci310_group_project.ui.login.LoginActivity;
import com.example.csci310_group_project.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DocSnippets";
    public String user = "";
    private static boolean initial = true;
    
    private String popularEvents = "Loading popular events...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        TextView nicknameText = findViewById(R.id.username);

        if(intent.getStringExtra("user") != null && !intent.getStringExtra("user").equals("")){
            user = intent.getStringExtra("user");
            View b = findViewById(R.id.login_button);
            b.setVisibility(View.GONE);
            View c = findViewById(R.id.register_button);
            c.setVisibility(View.GONE);
            Button p1_button = (Button)findViewById(R.id.explore_button);
            p1_button.setText("Explore Events!");

            // display nickname
            displayNickname(user, nicknameText);

        } else {
            View welcomeMsg = findViewById(R.id.welcome_msg);
            welcomeMsg.setVisibility(View.GONE);

            nicknameText.setVisibility(View.GONE);
        }

        TextView text = findViewById(R.id.login_button);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"restart", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });


        TextView reg = findViewById(R.id.register_button);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"restart", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        TextView exploreButton = findViewById(R.id.explore_button);
        exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRedirectToExplore();
            }
        });

        if(initial){
            initial = false;
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked

                            break;
                    }
                }
            };

            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Welcome! Please login/register to access full functionalities!").setPositiveButton("Yes", dialogClickListener).show();
        }

        setPopularEvents();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, popularEvents, Snackbar.LENGTH_LONG).setTextMaxLines(10)
                        .setAction("Action", null).show();
            }
        });
    }

    public void setPopularEvents(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("allEvent")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<Event> eventsList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("EventName", document.getString("name"));
                                Log.d("EventLng", String.valueOf(document.getDouble("lng")));
                                eventsList.add(new Event(document.getString("name"), document.getString("type"),
                                        document.getString("date"), document.getString("sponsoring_org"), document.getString("description"),
                                        document.getString("location"), (int) (long) (document.getLong("cost")),0, false, false,
                                        document.getDouble("lat"), document.getDouble("lng"), (int)(long)(document.getLong("registered"))));
                            }

                            eventsList.sort(Comparator.comparing(Event::getEventPopularity).reversed());
                            popularEvents = "Top Three Most Popular Events: \n\n1. " + eventsList.get(0).getEventName() + ";\n2. " +
                                    eventsList.get(1).getEventName() + ";\n3. " + eventsList.get(2).getEventName() + ";";

                        } else {
                            Log.d("EventError", "Error getting documents: ", task.getException());
                            Toast.makeText(MainActivity.this, "Something is wrong...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onRedirectToExplore(){
//        Intent intent = new Intent(this, ExploreActivity.class);
        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void displayNickname(String user, TextView nicknameText) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists() && document.getString("nickname") != null) {
                            nicknameText.setText(document.getString("nickname"));
                        } else {
                            nicknameText.setText(user);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed(){

    }
}