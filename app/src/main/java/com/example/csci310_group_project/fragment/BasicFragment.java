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

import androidx.activity.OnBackPressedCallback;
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
import android.view.KeyEvent;
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
import java.security.Key;
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

public class BasicFragment extends Fragment  implements OnMapReadyCallback,
        LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener{
    protected ArrayList<Event> eventsList;
    protected ArrayList<Event> filteredEventList = eventsList;
    protected Context context;
    protected String user = "";

    // for filtering
    protected RecyclerView recyclerView;
    protected recyclerAdapter.RecyclerViewClickListener listener;
    protected SearchView searchView;
    protected recyclerAdapter mAdapter;
    protected Spinner sortSpinner;
    protected Spinner typeSpinner;
    protected String searchText;

    // for date picker
    protected DatePickerDialog dateFromPickerDialog;
    protected DatePickerDialog dateToPickerDialog;
    protected Button dateFromButton;
    protected Button dateToButton;

    protected GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 999;
    GoogleApiAvailability googleAPI;
    SupportMapFragment mapFragment;

    Boolean initialized = false;

    protected double currLat = 0.;
    protected double currLong = 0.;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    protected static final String ARG_PARAM1 = "param1";
    protected static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    protected String mParam1;
    protected String mParam2;

    public void setUser(String user) {
        this.user = user;
    }

    public BasicFragment() {
        // Required empty public constructor
    }

    public static BasicFragment newInstance(String param1, String param2) {
        BasicFragment fragment = new BasicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    protected boolean checkPlayServices() {
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

    protected void setAdapter() {
        setOnClickListener();
        recyclerAdapter adapter = new recyclerAdapter(eventsList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        mAdapter = adapter;
    }

    protected void setOnClickListener() {
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

    protected void initDateFromPicker(View view)
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateFromButton.setText(date);

                // call filter
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

    protected void initDateToPicker(View view)
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateToButton.setText(date);

                // call filter
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

    public String makeDateString(int day, int month, int year)
    {
        return month + "/" + day + "/" + year;
    }

    protected void filterList(String text, String type, String sorting, String startDate, String endDate) {
        ArrayList<Event> filteredEventsList = new ArrayList<>(), sortedEventsList;
        String selectedType = type.toLowerCase();

        // TODO: NEW: get searched events
        filteredEventsList = GetSearchedEvents(eventsList, text, selectedType, startDate, endDate);
        filteredEventList = SortEvents(filteredEventsList, sorting.toLowerCase());
        mAdapter.SetFilteredList(filteredEventList);
    }

    protected Boolean isInDateRange(String startDate, String endDate, Event event) {
        String[] startDateParts = startDate.split("/");
        Integer startDateYear = Integer.valueOf(startDateParts[2]);
        Integer startDateMonth = Integer.valueOf(startDateParts[0]);
        Integer startDateDay = Integer.valueOf(startDateParts[1]);

        String[] endDateParts = endDate.split("/");
        Integer endDateYear = Integer.valueOf(endDateParts[2]);
        Integer endDateMonth = Integer.valueOf(endDateParts[0]);
        Integer endDateDay = Integer.valueOf(endDateParts[1]);

        Integer startDateInteger = CalcDateInteger(startDateYear, startDateMonth, startDateDay);
        Integer endDateInteger = CalcDateInteger(endDateYear, endDateMonth, endDateDay);
        Integer eventDateInteger = CalcDateInteger(event.getEventYear(), event.getEventMonth(), event.getEventDay());

        return eventDateInteger >= startDateInteger && eventDateInteger <= endDateInteger;
    }

    public Integer CalcDateInteger(Integer year, Integer month, Integer day) {
        return year * 10000 + month * 100 + day;
    }

    protected void setSearchView(View view) {
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

    protected void setSortSpinner(View view) {
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

    protected void setTypeSpinner(View view) {
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
    protected ActivityResultLauncher<String> requestPermissionLauncher =
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

    protected double distance(double lat1, double lat2, double lon1, double lon2, char unit) {
        Log.d("Calculated", String.valueOf(lat1) + " " + String.valueOf(lat2) + " " + String.valueOf(lon1) + " " + String.valueOf(lon2));
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

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {} // disable onBackPressed()
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public ArrayList<Event> GetSearchedEvents(ArrayList<Event> eventsList, String text, String selectedType, String startDate, String endDate) {
        ArrayList<Event> filteredEventsList = new ArrayList<>();

        for (Event event : eventsList) {
            String eventName = event.getEventName().toLowerCase();
            String eventType = event.getEventType().toLowerCase();
            String eventLocation = event.getEventLocation().toLowerCase();
            String eventOrg = event.getEventOrganizor().toLowerCase();

            if (text == null || text.isEmpty() || eventName.contains(text.toLowerCase()) || eventLocation.contains(text.toLowerCase()) || eventOrg.contains(text.toLowerCase())) {
                if (selectedType.contains("all") || eventType.contains(selectedType)) {
                    if (isInDateRange(startDate, endDate, event)) {
                        filteredEventsList.add(event);
                    }
                }
            }
        }

        return filteredEventsList;
    }

    public ArrayList<Event> SortEvents(ArrayList<Event> filteredEventsList, String sorting){

        // sort via the selected value of the sort spinner
        if (sorting.toLowerCase().contains("cost")) { // sort via cost
            filteredEventsList.sort(Comparator.comparing(Event::getEventCost));

        } else if (sorting.toLowerCase().contains("distance")){ // sort via distance
            filteredEventsList.sort(Comparator.comparing(Event::getDistanceToUser));

        } else if (sorting.toLowerCase().contains("time")){ // sort via time
            filteredEventsList.sort(Comparator.comparing(Event::getEventYear)
                    .thenComparing((Event::getEventMonth))
                    .thenComparing((Event::getEventDay))
                    .thenComparing(Event::getEventHour)
                    .thenComparing(Event::getEventMinute));

        } else if (sorting.toLowerCase().contains("alphabetic")){
            filteredEventsList.sort(Comparator.comparing(Event::getEventName));

        } else if (sorting.toLowerCase().contains("popularity")) {
            filteredEventsList.sort(Comparator.comparing(Event::getEventPopularity).reversed());
        }

        return filteredEventsList;
    }

    public void SetUserForTestingPurpose(String u) {
        user = u;
    }
}
