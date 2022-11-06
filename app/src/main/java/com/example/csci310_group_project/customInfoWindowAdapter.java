package com.example.csci310_group_project;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

public class customInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View window;
    private Context context;
    private GestureDetector mDetector;


    public customInfoWindowAdapter(Context context){
        this.context = context;
        window = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);

    }

    public void windowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView myView = (TextView) view.findViewById(R.id.infoText);
        myView.setText(title + "\n\n" + "Click her2 for more details!");
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        windowText(marker, window);
        return window;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        windowText(marker, window);
        return window;
    }
}
