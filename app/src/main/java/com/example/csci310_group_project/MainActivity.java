package com.example.csci310_group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.csci310_group_project.ui.login.LoginActivity;
import com.example.csci310_group_project.ui.register.RegisterActivity;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DocSnippets";
    public String user = "";

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

        Intent intent = getIntent();
        TextView usernameDisplay = findViewById(R.id.username);

        if(intent.getStringExtra("user") != null && !intent.getStringExtra("user").equals("")){
            user = intent.getStringExtra("user");
            View b = findViewById(R.id.login_button);
            b.setVisibility(View.GONE);
            View c = findViewById(R.id.register_button);
            c.setVisibility(View.GONE);
            Button p1_button = (Button)findViewById(R.id.explore_button);
            p1_button.setText("Explore Events!");

            // display username
            usernameDisplay.setText(user);

        } else {
            View welcomeMsg = findViewById(R.id.welcome_msg);
            welcomeMsg.setVisibility(View.GONE);

            usernameDisplay.setVisibility(View.GONE);
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
}