package com.example.csci310_group_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.example.csci310_group_project.Event;
import com.example.csci310_group_project.EventDetailActivity;
import com.example.csci310_group_project.R;
import com.example.csci310_group_project.recyclerAdapter;

import java.util.ArrayList;

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

        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });

        // TODO: spinner onChange

        recyclerView = view.findViewById(R.id.recyclerView);
        eventsList = new ArrayList<>();

        setEventInfo();
        setAdapter();

        return view;
    }

    // TODO: check if date and sort types are set
    private void filterList(String text) {
        ArrayList<Event> filteredEventsList = new ArrayList<>();

        for (Event event : eventsList) {
            if (event.getEventName().toLowerCase().contains(text.toLowerCase())) {
                filteredEventsList.add(event);
            }
        }

        if (filteredEventsList.isEmpty()) {
            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.SetFilteredList(filteredEventsList);
        }
    }

    // TODO: read from DAO
    private void setEventInfo(){
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
    }
}