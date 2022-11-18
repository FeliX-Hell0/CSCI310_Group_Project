package com.example.csci310_group_project.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csci310_group_project.Event;
import com.example.csci310_group_project.EventDetailActivity;
import com.example.csci310_group_project.MainActivity;
import com.example.csci310_group_project.R;
import com.example.csci310_group_project.recyclerAdapter;
import com.example.csci310_group_project.ui.login.LoginActivity;
import com.example.csci310_group_project.ui.register.RegisterActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BasicFragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if(user == null || user.equals("")){
            Toast.makeText(getActivity(), "Please login or register to access profile",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
        }

    }

    @Override
    public void setAdapter() {
        setOnClickListener();
        if(!user.equals("")) {
            recyclerAdapter adapter = new recyclerAdapter(eventsList, listener);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            mAdapter = adapter;
        }
        else{
            List<Event> emptyList = new ArrayList<>();
            recyclerAdapter adapter = new recyclerAdapter((ArrayList<Event>) emptyList, listener);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            mAdapter = adapter;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        setSearchView(view);
        setSortSpinner(view);
        setTypeSpinner(view);


        recyclerView = view.findViewById(R.id.recyclerView);
        eventsList = new ArrayList<>();
        showImage(view);
        setEventInfo();
        setAdapter();

        initDateFromPicker(view);
        initDateToPicker(view);

        initUserInfo(view);
        initLogoutButton(view);
        initScrollButton(view);

        return view;
    }

    private void initScrollButton(View view) {
        Button scroll = view.findViewById(R.id.scroll_button);
        ScrollView scrollView = view.findViewById(R.id.scroll_view);

        scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.smoothScrollTo(0,1500);
            }
        });
    }


    private void initUserInfo(View view)
    {
        // set username
        TextView username = view.findViewById(R.id.username);

        // set user b-day
        TextView birthday = view.findViewById(R.id.user_birthday);
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists() && document.getString("birthday") != null) {
                            birthday.setText("B-Day: " + document.getString("birthday"));
                        }
                        if (document.exists() && document.getString("nickname") != null) {
                            username.setText("Nickname: " + document.getString("nickname"));
                        } else {
                            username.setText(user);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventInfo(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("allEvent")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            eventsList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                eventsList.add(new Event(document.getString("name"), document.getString("type"),
                                        document.getString("date"), document.getString("sponsoring_org"), document.getString("description"),
                                        document.getString("location"), (int) (long) (document.getLong("cost")),0, false, false,
                                        document.getDouble("lat"), document.getDouble("lng")));
                            }

                            // by default sort by cost
                            eventsList.sort(Comparator.comparing(Event::getEventCost));
                            // Toast.makeText(getActivity(), "Load Events Success", Toast.LENGTH_SHORT).show();
                            filteredEventList = eventsList;
                            setRegisteredEvents();
                            setFavoriteEvents();
                            setAdapter();

                        } else {
                            Log.d("EventError", "Error getting documents: ", task.getException());
                            Toast.makeText(getActivity(), "Something is wrong...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setRegisteredEvents(){
        if(user.equals("")){
            Toast.makeText(getActivity(), "Guest mode", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Toast.makeText(getActivity(), "Fetching...", Toast.LENGTH_SHORT).show();
                        if (!document.getString("registeredEvents").equals("")) {
                            String collection = document.getString("registeredEvents");
                            String[] registeredEvents = collection.split(";");
                            List<String> regEvents = new ArrayList<>(Arrays.asList(registeredEvents));

                            if(regEvents != null) {
                                for (String event : regEvents) {
                                    for (Event myEvent : eventsList) {
                                        if (myEvent.getEventName().equals(event)) {
                                            myEvent.setRegistered(true);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "No user registration info", Toast.LENGTH_SHORT).show();
                    }
                    filterUnReg();
                    filteredEventList = eventsList;
                    setAdapter();

                } else {
                    Toast.makeText(getActivity(), "Connection error", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void setFavoriteEvents(){
        if(user.equals("")){
            Toast.makeText(getActivity(), "Guest mode", Toast.LENGTH_SHORT).show();
            return;
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Toast.makeText(getActivity(), "Fetching...", Toast.LENGTH_SHORT).show();
                        if (document.getString("favorites") != null && !document.getString("favorites").equals("")) {
                            String collection = document.getString("favorites");
                            String[] favoriteEvents = collection.split(";");
                            List<String> favEvents = new ArrayList<>(Arrays.asList(favoriteEvents));

                            if(favEvents != null) {
                                for (String event : favEvents) {
                                    for (Event myEvent : eventsList) {
                                        if (myEvent.getEventName().equals(event)) {
                                            myEvent.setFavorite(true);
                                            Log.d("FavCount", "Hi");
                                        }
                                    }
                                }
                            }

                            filterUnReg();
                        }
                    } else {
                        Toast.makeText(getActivity(), "No user favorites info", Toast.LENGTH_SHORT).show();
                    }
                    filteredEventList = eventsList;
                    setAdapter();

                } else {
                    Toast.makeText(getActivity(), "Connection error", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void filterUnReg(){
        ArrayList<Event> temp = new ArrayList<>();
        for(Event e : eventsList){
            if(e.getRegistered()){
                temp.add(e);
            }
        }

        eventsList.clear();
        for(Event e : temp){
            eventsList.add(e);
        }
    }

    private void initLogoutButton(View view) {
        Button logoutButton = view.findViewById(R.id.logout_button);

        // add onclick
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("log", "logging out!");
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra("user", "");
                startActivity(i);
            }
        });
    }

    //assume user is the email
    private void showImage(View view){
        ImageView imageView = (ImageView) view.findViewById(R.id.user_image);
        StorageReference mStorage = FirebaseStorage.getInstance().getReference("userImage").child(user+".png");
        try {
            final File local = File.createTempFile("profile","png");
            mStorage.getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(local.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}