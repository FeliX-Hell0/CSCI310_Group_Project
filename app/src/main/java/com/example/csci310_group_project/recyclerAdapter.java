package com.example.csci310_group_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<Event> eventsList;
    private RecyclerViewClickListener listener;

    public void SetFilteredList(ArrayList<Event> filteredList) {
        this.eventsList = filteredList;
        notifyDataSetChanged();
    }

    public recyclerAdapter(ArrayList<Event> eventsList, RecyclerViewClickListener listener) {
        this.eventsList = eventsList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private ImageView img;

        private TextView dateText;
        private TextView nameText;
        private TextView locationText;
        private TextView organizorText;
        private TextView costText;
        private TextView typeText;
        private TextView registerText;
        private TextView favoriteText;

        public MyViewHolder(final View view) {
            super(view);

//            img = view.findViewById(R.id.custom_event_box_img_view);

            dateText = view.findViewById(R.id.custom_event_date);
            nameText = view.findViewById(R.id.custom_event_title);
            locationText = view.findViewById(R.id.custom_event_location);
            costText = view.findViewById(R.id.custom_event_cost);
            organizorText = view.findViewById(R.id.custom_event_organizer);
            typeText = view.findViewById(R.id.custom_event_type);
            registerText = view.findViewById(R.id.custom_event_register_status);
            favoriteText = view.findViewById(R.id.custom_event_favorite_status);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_event_box_cardview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        // TODO: deal with images
        String name = eventsList.get(position).getEventName();
        String date = eventsList.get(position).getEventDate();
        String location = eventsList.get(position).getEventLocation();
//        String organizer = eventsList.get(position).getEventOrganizer();
        String type = eventsList.get(position).getEventType();
        int cost = eventsList.get(position).getEventCost();
        boolean registered = eventsList.get(position).getRegistered();
        boolean favorite = eventsList.get(position).getFavorite();

        holder.nameText.setText(name);
        holder.dateText.setText(date);
        holder.locationText.setText(location);
        holder.typeText.setText(type);
        holder.costText.setText("$" + String.valueOf(cost));

        if(registered){
            holder.registerText.setText("Registered");
        }
        else{
            holder.registerText.setText("Not Registered");
        }

        if(favorite){
            holder.favoriteText.setText("★");
        }
        else{
            holder.favoriteText.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
