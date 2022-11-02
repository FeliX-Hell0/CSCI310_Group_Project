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
import java.util.Locale;

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
    private Spinner sortSpinner;
    private Spinner typeSpinner;
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

        setSearchView(view);
        setSortSpinner(view);
        setTypeSpinner(view);

        recyclerView = view.findViewById(R.id.recyclerView);
        eventsList = new ArrayList<>();

        setEventInfo();
        setAdapter();

        return view;
    }

    private void filterList(String text, String type, String sorting) {
        ArrayList<Event> filteredEventsList = new ArrayList<>();
        String selectedType = type.toLowerCase();

        for (Event event : eventsList) {

            String eventName = event.getEventName().toLowerCase();
            String eventType = event.getEventType().toLowerCase();

            if (text == null || text.isEmpty() || eventName.contains(text.toLowerCase())) {
                if (selectedType.contains("all") || eventType.contains(selectedType)) {
                    filteredEventsList.add(event);
                }
            }
        }


        // TODO: sort via spinnerValue
        if (sorting.toLowerCase().contains("cost")) {
            // sort via cost
            filteredEventsList.sort(Comparator.comparing(Event::getEventCost));

        } else if (sorting.toLowerCase().contains("distance")){
            // sort via distance
            // TODO: get user address
            // TODO: longitude & latitude

        } else if (sorting.toLowerCase().contains("time")){
            // sort via time
            // TODO: compare time




        } else if (sorting.toLowerCase().contains("alphabetic")){
//            Toast.makeText(getActivity(), "sort via alphabetic", Toast.LENGTH_LONG).show();
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

                        // by default sort by cost
                        eventsList.sort(Comparator.comparing(Event::getEventCost));
                        Toast.makeText(getActivity(), "Load Events Success", Toast.LENGTH_LONG).show();
                        setAdapter();

                    } else {
                        Log.d("EventError", "Error getting documents: ", task.getException());
                        Toast.makeText(getActivity(), "Something is wrong...", Toast.LENGTH_LONG).show();
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
                filterList(s, type, sorting);
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
                String type = "all";
                filterList(searchText, type, sorting);
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
                filterList(searchText, type, sorting);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }
}