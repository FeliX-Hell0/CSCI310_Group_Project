package com.example.csci310_group_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.csci310_group_project.databinding.ActivityContentBinding;
import com.example.csci310_group_project.fragment.ExploreFragment;
import com.example.csci310_group_project.fragment.ProfileFragment;
import com.example.csci310_group_project.fragment.MapFragment;

public class ContentActivity extends AppCompatActivity {

    ActivityContentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ExploreFragment()); // init

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_explore:
                    replaceFragment(new ExploreFragment());
                    break;
                case R.id.nav_map:
                    replaceFragment(new MapFragment());
                    break;
                case R.id.nav_profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}