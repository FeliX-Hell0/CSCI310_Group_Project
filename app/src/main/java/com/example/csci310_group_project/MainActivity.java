package com.example.csci310_group_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.csci310_group_project.ui.login.LoginActivity;
import com.example.csci310_group_project.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DocSnippets";
    public String user = "";

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