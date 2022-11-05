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
public class ExploreFragment extends Fragment implements OnMapReadyCallback,
        LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener{
    private ArrayList<Event> eventsList;
    private ArrayList<Event> filteredEventList = eventsList;
    private Context context;
    private String user = "";

    // for filtering
    private RecyclerView recyclerView;
    private recyclerAdapter.RecyclerViewClickListener listener;
    private SearchView searchView;
    private recyclerAdapter mAdapter;
    private Spinner sortSpinner;
    private Spinner typeSpinner;
    private String searchText;

    // for date picker
    private DatePickerDialog dateFromPickerDialog;
    private DatePickerDialog dateToPickerDialog;
    private Button dateFromButton;
    private Button dateToButton;

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 999;
    GoogleApiAvailability googleAPI;
    SupportMapFragment mapFragment;

    Boolean initialized = false;

    private double currLat = 0.;
    private double currLong = 0.;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public void setUser(String user) {
        this.user = user;
    }

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

    private boolean checkPlayServices() {
        googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

    private void setAdapter() {
        setOnClickListener();
        recyclerAdapter adapter = new recyclerAdapter(eventsList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        mAdapter = adapter;
    }

    private void setOnClickListener() {
        listener = new recyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(context.getApplicationContext(), EventDetailActivity.class);
                Log.d("EventName", filteredEventList.get(position).getEventName());
                intent.putExtra("event_name", filteredEventList.get(position).getEventName());
                intent.putExtra("user", user);
                intent.putExtra("register", filteredEventList.get(position).getRegistered());
                intent.putExtra("favorite", filteredEventList.get(position).getFavorite());
                startActivity(intent);
            }
        };
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



    private void initDateFromPicker(View view)
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateFromButton.setText(date);

                // TODO: call filter
                String sorting = sortSpinner.getSelectedItem().toString();
                String type = typeSpinner.getSelectedItem().toString();
                String startDate = date;
                String endDate = dateToButton.getText().toString();
                filterList(searchText, type, sorting, startDate, endDate);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        dateFromPickerDialog = new DatePickerDialog(context, style, dateSetListener, year, month, day);
        dateFromButton = view.findViewById(R.id.dateFromPickerButton);
        dateFromButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dateFromPickerDialog.show();
            }
        });
    }

    private void initDateToPicker(View view)
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateToButton.setText(date);

                // TODO: call filter
                String sorting = sortSpinner.getSelectedItem().toString();
                String type = typeSpinner.getSelectedItem().toString();
                String startDate = dateFromButton.getText().toString();
                String endDate = date;
                filterList(searchText, type, sorting, startDate, endDate);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        dateToPickerDialog = new DatePickerDialog(context, style, dateSetListener, year, month, day);
        dateToButton = view.findViewById(R.id.dateToPickerButton);
        dateToButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dateToPickerDialog.show();
            }
        });
    }

    private String makeDateString(int day, int month, int year)
    {
        return month + "/" + day + "/" + year;
    }

    private void filterList(String text, String type, String sorting, String startDate, String endDate) {
        ArrayList<Event> filteredEventsList = new ArrayList<>();
        String selectedType = type.toLowerCase();

        for (Event event : eventsList) {
            String eventName = event.getEventName().toLowerCase();
            String eventType = event.getEventType().toLowerCase();

            if (text == null || text.isEmpty() || eventName.contains(text.toLowerCase())) {
                if (selectedType.contains("all") || eventType.contains(selectedType)) {
                    if (isInDateRange(startDate, endDate, event)) {
                        filteredEventsList.add(event);
                    }
                }
            }
        }

        // sort via the selected value of the sort spinner
        if (sorting.toLowerCase().contains("cost")) { // sort via cost
            filteredEventsList.sort(Comparator.comparing(Event::getEventCost));

        } else if (sorting.toLowerCase().contains("distance")){ // sort via distance
            // TODO: get user address
            // TODO: longitude & latitude
            filteredEventsList.sort(Comparator.comparing(Event::getDistanceToUser));

        } else if (sorting.toLowerCase().contains("time")){ // sort via time
            filteredEventsList.sort(Comparator.comparing(Event::getEventYear)
                    .thenComparing((Event::getEventMonth))
                    .thenComparing((Event::getEventDay))
                    .thenComparing(Event::getEventHour)
                    .thenComparing(Event::getEventMinute));

        } else if (sorting.toLowerCase().contains("alphabetic")){
            filteredEventsList.sort(Comparator.comparing(Event::getEventName));
        }
        filteredEventList = filteredEventsList;
        mAdapter.SetFilteredList(filteredEventsList);
    }

    private Boolean isInDateRange(String startDate, String endDate, Event event) {
        String[] startDateParts = startDate.split("/");
        Integer startDateYear = Integer.valueOf(startDateParts[2]);
        Integer startDateMonth = Integer.valueOf(startDateParts[0]);
        Integer startDateDay = Integer.valueOf(startDateParts[1]);

        String[] endDateParts = endDate.split("/");
        Integer endDateYear = Integer.valueOf(endDateParts[2]);
        Integer endDateMonth = Integer.valueOf(endDateParts[0]);
        Integer endDateDay = Integer.valueOf(endDateParts[1]);

        Integer startDateInteger = startDateYear * 10000 + startDateMonth * 100 + startDateDay;
        Integer endDateInteger = endDateYear * 10000 + endDateMonth * 100 + endDateDay;
        Integer eventDateInteger = event.getEventYear() * 10000 + event.getEventMonth() * 100 + event.getEventDay();

        if (eventDateInteger >= startDateInteger && eventDateInteger <= endDateInteger) {
            return true;
        }

        return false;
    }

    // TODO: read from DAO
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
//                            Log.d("Event", document.getId() + " => " + document.getData());
                            Log.d("EventName", document.getString("name"));
                            Log.d("EventLat", String.valueOf(document.getDouble("lng")));
                            eventsList.add(new Event(document.getString("name"), document.getString("type"),
                                    document.getString("date"), document.getString("sponsoring_org"), document.getString("description"),
                                    document.getString("location"), (int) (long) (document.getLong("cost")),0, false, false,
                                    document.getDouble("lat"), document.getDouble("lng")));
                        }

                        // by default sort by cost

                        for(Event e : eventsList){
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

    private void setSearchView(View view) {
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchText = s;
                String sorting = sortSpinner.getSelectedItem().toString();
                String type = "all";
                String startDate = dateFromButton.getText().toString();
                String endDate = dateToButton.getText().toString();
                filterList(s, type, sorting, startDate, endDate);
                return true;
            }
        });
    }

    private void setSortSpinner(View view) {
        sortSpinner = view.findViewById(R.id.sort_spinner);

        ArrayAdapter<CharSequence> sortArrayAdapter = ArrayAdapter.createFromResource(context, R.array.Sorts, android.R.layout.simple_spinner_item);
        sortArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortArrayAdapter);

        // call filterList when onSelect
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String sorting = sortSpinner.getSelectedItem().toString();
                String type = typeSpinner.getSelectedItem().toString();
                String startDate = dateFromButton.getText().toString();
                String endDate = dateToButton.getText().toString();
                filterList(searchText, type, sorting, startDate, endDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void setTypeSpinner(View view) {
        typeSpinner = view.findViewById(R.id.type_spinner);

        ArrayAdapter<CharSequence> TypeArrayAdapter = ArrayAdapter.createFromResource(context, R.array.Types, android.R.layout.simple_spinner_item);
        TypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(TypeArrayAdapter);

        // call filterList when onSelect
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String sorting = sortSpinner.getSelectedItem().toString();
                String type = typeSpinner.getSelectedItem().toString();
                String startDate = dateFromButton.getText().toString();
                String endDate = dateToButton.getText().toString();
                filterList(searchText, type, sorting, startDate, endDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //move map camera
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("CurrentLat", String.valueOf(eventsList.size()));

        currLat = location.getLatitude();
        currLong = location.getLongitude();

        for(Event e : eventsList){
            e.setDistanceToUser(distance(currLat, e.getLat(), currLong, e.getLng(), 'K'));

        }

        for(Event e : eventsList){
            e.setDistanceToUser(distance(currLat, e.getLat(), currLong, e.getLng(), 'K'));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        Log.d("MapPermission", "Here");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }else{
                Log.d("MapPermission2", "Here");
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
        else {
            Log.d("MapPermission1", "Here");
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @SuppressLint("MissingPermission")
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                Log.d("MapPermission5", "Here");
                if (isGranted) {
                    Log.d("MapPermission4", "Here");
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    private double distance(double lat1, double lat2, double lon1, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}