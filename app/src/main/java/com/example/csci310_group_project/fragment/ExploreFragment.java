package com.example.csci310_group_project.fragment;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.csci310_group_project.Event;
import com.example.csci310_group_project.EventDetailActivity;
import com.example.csci310_group_project.R;
import com.example.csci310_group_project.recyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {
    private ArrayList<Event> eventsList;
    private RecyclerView recyclerView;
    private recyclerAdapter.RecyclerViewClickListener listener;
    private SearchView searchView;
    private recyclerAdapter mAdapter;
    private Spinner spinner;
    private String searchText;


    private Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
                intent.putExtra("EVENT_INDEX", String.valueOf(position));
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
        spinner = view.findViewById(R.id.sort_spinner);

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
                filterList(s, spinner.getSelectedItem().toString());
                return true;
            }
        });

        // spinner onChange
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(context, R.array.Sorts, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        // call filterList when onSelect
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String spinnerVal = spinner.getSelectedItem().toString();
                filterList(searchText, spinnerVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        recyclerView = view.findViewById(R.id.recyclerView);
        eventsList = new ArrayList<>();

        setEventInfo();
        setAdapter();

        return view;
    }

    // TODO: check if date and sort types are set
    private void filterList(String text, String spinnerValue) {
        ArrayList<Event> filteredEventsList = new ArrayList<>();

        for (Event event : eventsList) {
            if (text == null || text.isEmpty()) {
                filteredEventsList.add(event);
            } else if (event.getEventName().toLowerCase().contains(text.toLowerCase())) {
                filteredEventsList.add(event);
            }
        }


        // TODO: sort via spinnerValue
        if (spinnerValue.toLowerCase().contains("cost")) {
            Toast.makeText(getActivity(), "sort via cost", Toast.LENGTH_LONG).show();
            // sort via cost
            filteredEventsList.sort(Comparator.comparing(Event::getEventCost));

        } else if (spinnerValue.toLowerCase().contains("distance")){
            // sort via distance
            // TODO: get user address
            // TODO: longitude & latitude

        } else if (spinnerValue.toLowerCase().contains("time")){
            // sort via time
            // TODO: compare time

        } else if (spinnerValue.toLowerCase().contains("alphabetic")){
            // sort via alphabetical order
            Toast.makeText(getActivity(), "sort via alphabetic", Toast.LENGTH_LONG).show();
            filteredEventsList.sort(Comparator.comparing(Event::getEventName));
        }

        mAdapter.SetFilteredList(filteredEventsList);
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
                                Log.d("Event", document.getId() + " => " + document.getData());
                                Log.d("EventName", String.valueOf(document.getLong("cost")));
                                eventsList.add(new Event(document.getString("name"), document.getString("type"),
                                        document.getString("date"), document.getString("sponsoring_org"), document.getString("description"),
                                        document.getString("location"), (int) (long) (document.getLong("cost")),0));
                            }
                            eventsList.sort(Comparator.comparing(Event::getEventCost));
                            Toast.makeText(getActivity(), "Load Events Success", Toast.LENGTH_LONG).show();
                            setAdapter();

                        } else {
                            Log.d("EventError", "Error getting documents: ", task.getException());
                            Toast.makeText(getActivity(), "Something is wrong...", Toast.LENGTH_LONG).show();
                        }
                    }
                });



        /*
        eventsList.add(new Event(
                "Banda Los Recoditos, Hijos De Barron ", "party", "Oct 10, 2022 8:00 PM", "UUUUSC", "gonna be fun", "Viterbi RRB", 30, 0
        ));
        eventsList.add(new Event(
                "Fall Festival AVANA", "party","Dec 20, 2022 3:00 PM", "UCLA", "gonna be interesting", "UCLA Campus", 20, 0
        ));
        eventsList.add(new Event(
                "Sunset Vibes Silect Disco Special Party", "party","Nov 06, 2022 5:30 PM", "UCLA", "gonna be interesting", "@Vista / Hermosa Beach", 20, 0
        ));
        eventsList.add(new Event(
                "Banda Los Recoditos, Hijos De Barron ", "party","Oct 10, 2022 8:00 PM", "UUUUSC", "gonna be fun", "Viterbi RRB", 30, 0
        ));
        eventsList.add(new Event(
                "Fall Festival AVANA", "Dec 20, 2022 3:00 PM", "party","UCLA", "gonna be interesting", "UCLA Campus", 20, 0
        ));
        eventsList.add(new Event(
                "Sunset Vibes Silect Disco Special Party", "party","Nov 06, 2022 5:30 PM", "UCLA", "gonna be interesting", "@Vista / Hermosa Beach", 20, 0
        ));
        eventsList.add(new Event(
                "Banda Los Recoditos, Hijos De Barron ", "party","Oct 10, 2022 8:00 PM", "UUUUSC", "gonna be fun", "Viterbi RRB", 30, 0
        ));
        eventsList.add(new Event(
                "Fall Festival AVANA", "party","Dec 20, 2022 3:00 PM", "UCLA", "gonna be interesting", "UCLA Campus", 20, 0
        ));
        eventsList.add(new Event(
                "Sunset Vibes Silect Disco Special Party","party", "Nov 06, 2022 5:30 PM", "UCLA", "gonna be interesting", "@Vista / Hermosa Beach", 20, 0
        ));

         */
    }
}