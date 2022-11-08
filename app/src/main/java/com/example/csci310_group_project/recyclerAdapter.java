package com.example.csci310_group_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
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

        private TextView dateText;
        private TextView nameText;
        private TextView locationText;
        private TextView organizorText;
        private TextView costText;
        private TextView typeText;
        private TextView registerText;
        private TextView favoriteText;
        private ImageView imageView;

        public MyViewHolder(final View view) {
            super(view);

            dateText = view.findViewById(R.id.custom_event_date);
            nameText = view.findViewById(R.id.custom_event_title);
            locationText = view.findViewById(R.id.custom_event_location);
            costText = view.findViewById(R.id.custom_event_cost);
            organizorText = view.findViewById(R.id.custom_event_organizer);
            typeText = view.findViewById(R.id.custom_event_type);
            registerText = view.findViewById(R.id.custom_event_register_status);
            favoriteText = view.findViewById(R.id.custom_event_favorite_status);
            imageView = view.findViewById(R.id.custom_event_box_img_view);

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

        String name = eventsList.get(position).getEventName();
        String date = eventsList.get(position).getEventDate();
        String location = eventsList.get(position).getEventLocation();
        String type = eventsList.get(position).getEventType();
        String org = eventsList.get(position).getEventOrganizor();
        int cost = eventsList.get(position).getEventCost();
        boolean registered = eventsList.get(position).getRegistered();
        boolean favorite = eventsList.get(position).getFavorite();
        String imageName = name;
        if (name.equals("Abbot Kidding: A Comedy Show in Venice") ){
            imageName = "Abbot Kidding";
        }else if (name.equals("The Setup Presents: Citizen Public Market Comedy Night")){
            imageName = "Citizen Public Market Comedy Night";
        }else if (name.equals("Joachim Horsley: Caribbean Nocturnes In Concert")){
            imageName = "Caribbean Nocturnes In Concert";
        }else if (name.equals("MoreLuv: RnB")){
            imageName = "MoreLuv & RnB";
        }

        holder.nameText.setText(name);
        holder.dateText.setText(date);

        if (location.length() > 45) {
            location = location.substring(0, 45) + "...";
            Log.i("location", location);
        }
        holder.locationText.setText(location);
        holder.typeText.setText(type);
        holder.costText.setText("$" + String.valueOf(cost));
        holder.organizorText.setText(org);

        StorageReference mStorage = FirebaseStorage.getInstance().getReference("eventImage").child(imageName+".png");
        try {
            final File local = File.createTempFile("event","png");
            mStorage.getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(local.getAbsolutePath());
                    holder.imageView.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

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
