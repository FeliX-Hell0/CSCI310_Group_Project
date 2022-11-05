package com.example.csci310_group_project.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {
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
//                            Log.d("EventName", String.valueOf(document.getLong("cost")));
                            eventsList.add(new Event(document.getString("name"), document.getString("type"),
                                    document.getString("date"), document.getString("sponsoring_org"), document.getString("description"),
                                    document.getString("location"), (int) (long) (document.getLong("cost")),0, false, false));
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
}