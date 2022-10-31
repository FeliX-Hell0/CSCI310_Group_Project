package com.example.csci310_group_project.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.csci310_group_project.Event;
import com.example.csci310_group_project.R;
import com.example.csci310_group_project.recyclerAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {
    private static final int Event_COUNT = 20;
    private ArrayList<Event> eventsList;
    private RecyclerView recyclerView;


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
        recyclerAdapter adapter = new recyclerAdapter(eventsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        eventsList = new ArrayList<>();

        setEventInfo();
        setAdapter();

//        View view = inflater.inflate(R.layout.fragment_explore, container, false);
//        GridLayout layout = view.findViewById(R.id.grid_layout_event_list);
//
//        for (int i = 0; i < Event_COUNT; i++) {
//            GridLayout eventBox = (GridLayout) inflater.inflate(R.layout.custom_event_box_layout, layout, false);
//            Button button = eventBox.findViewById(R.id.custom_event_detail_button);
//
//            int finalI = i;
//            button.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    Log.i("event clicked", "button click");
//                    // sent intent to event detail page
//                    Intent intent = new Intent(getActivity(), EventDetailActivity.class);
//                    intent.putExtra("event_index", finalI);
//                    startActivity(intent);
//                }
//            });
//
//            GridLayout.LayoutParams lp = (GridLayout.LayoutParams) eventBox.getLayoutParams();
//            lp.rowSpec = GridLayout.spec(i);
//            lp.columnSpec = GridLayout.spec(0);
//            layout.addView(eventBox, lp);
//        }

        return view;
    }

    private void setEventInfo(){
        eventsList.add(new Event(
                "Banda Los Recoditos, Hijos De Barron ", "Oct 10, 2022 8:00 PM", "UUUUSC", "gonna be fun", "Viterbi RRB", 30, 0
        ));
        eventsList.add(new Event(
                "Fall Festival AVANA", "Dec 20, 2022 3:00 PM", "UCLA", "gonna be interesting", "UCLA Campus", 20, 0
        ));
        eventsList.add(new Event(
                "Sunset Vibes Silect Disco Special Party", "Nov 06, 2022 5:30 PM", "UCLA", "gonna be interesting", "@Vista / Hermosa Beach", 20, 0
        ));
    }
}