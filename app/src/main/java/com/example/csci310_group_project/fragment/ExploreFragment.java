package com.example.csci310_group_project.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.csci310_group_project.Event;
import com.example.csci310_group_project.EventDetailActivity;
import com.example.csci310_group_project.MainActivity;
import com.example.csci310_group_project.R;
import com.example.csci310_group_project.recyclerAdapter;
import com.example.csci310_group_project.ui.login.LoginActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends BasicFragment{

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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

        checkPlayServices();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        setSearchView(view);
        setSortSpinner(view);
        setTypeSpinner(view);

        recyclerView = view.findViewById(R.id.recyclerView);
        eventsList = new ArrayList<>();

        setEventInfo();
        setAdapter();

        initDateFromPicker(view);
        initDateToPicker(view);

        mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        mapFragment.getMapAsync(this);

        return view;
    }


    // TODO: read from DAO
    private void setEventInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("allEvent")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        eventsList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Log.d("Event", document.getId() + " => " + document.getData());
                            Log.d("EventName", document.getString("name"));
                            Log.d("EventLng", String.valueOf(document.getDouble("lng")));
                            eventsList.add(new Event(document.getString("name"), document.getString("type"),
                                    document.getString("date"), document.getString("sponsoring_org"), document.getString("description"),
                                    document.getString("location"), (int) (long) (document.getLong("cost")),0, false, false,
                                    document.getDouble("lat"), document.getDouble("lng")));
                        }

                        // by default sort by cost

                        for(Event e : eventsList){
                            Log.d("EventLng1", String.valueOf(e.getLng()));
                            e.setDistanceToUser(distance(currLat, e.getLat(), currLong, e.getLng(), 'K'));
                            Log.d("distanceCurr2", String.valueOf(e.getLng()));
                        }

                        for(Event e : eventsList){
                            e.setDistanceToUser(distance(currLat, e.getLat(), currLong, e.getLng(), 'K'));
                        }
                        setAdapter();

                        eventsList.sort(Comparator.comparing(Event::getEventCost));
                        // Toast.makeText(getActivity(), "Load Events Success", Toast.LENGTH_SHORT).show();
                        filteredEventList = eventsList;
                        setRegisteredEvents();
                        setFavoriteEvents();
                        //Log.d("EventLat", String.valueOf(eventsList.get(0).getLat()));


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

        //Toast.makeText(getActivity(), user, Toast.LENGTH_SHORT).show();

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
                            //Toast.makeText(getActivity(), "Registered events loading success", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getActivity(), "No registered events", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "No user registration info", Toast.LENGTH_SHORT).show();
                    }
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

        //Toast.makeText(getActivity(), user, Toast.LENGTH_SHORT).show();

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
                                        }
                                    }
                                }
                            }
                            //Toast.makeText(getActivity(), "Favorite events loading success", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getActivity(), "No favorite events", Toast.LENGTH_SHORT).show();
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

}