package com.example.csci310_group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.csci310_group_project.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DocSnippets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("username", "test");
        user.put("password", "123456");
        db.collection("test3").add(user);
        */


        TextView text = findViewById(R.id.login_button);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"restart", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        startActivity(intent);
    }
}