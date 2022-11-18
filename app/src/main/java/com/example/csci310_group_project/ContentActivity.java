package com.example.csci310_group_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.csci310_group_project.databinding.ActivityContentBinding;
import com.example.csci310_group_project.fragment.ExploreFragment;
import com.example.csci310_group_project.fragment.FavFragment;
import com.example.csci310_group_project.fragment.ProfileFragment;
import com.example.csci310_group_project.fragment.MapFragment;

public class ContentActivity extends AppCompatActivity {

    ActivityContentBinding binding;
    private String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContentBinding.inflate(getLayoutInflater());

        String fav = "";
        Intent intent = getIntent();
        if(intent.getStringExtra("user") != null && !intent.getStringExtra("user").equals("")){
            user = intent.getStringExtra("user");
            fav = intent.getStringExtra("toFav");
        }

        setContentView(binding.getRoot());
        ExploreFragment fragment = new ExploreFragment();
        fragment.setUser(user);
        replaceFragment(fragment); // init



        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_explore:
                    ExploreFragment fragment2 = new ExploreFragment();
                    fragment2.setUser(user);
                    replaceFragment(fragment2);
                    break;
                case R.id.nav_map:
                    MapFragment frag = new MapFragment();
                    frag.setUser(user);
                    replaceFragment(frag);
                    break;
                case R.id.nav_fav:
                    FavFragment favFragment = new FavFragment();
                    favFragment.setUser(user);
                    replaceFragment(favFragment);
                    break;
                case R.id.nav_profile:
                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setUser(user);
                    replaceFragment(profileFragment);
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.setReorderingAllowed(true);
        transaction.addToBackStack("");
        transaction.commit();
    }
}