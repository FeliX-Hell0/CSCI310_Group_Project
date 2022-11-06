package com.example.csci310_group_project;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private View window;
    private Context context;
    public static String title = "Loading";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        window = inflater.inflate(R.layout.bottom_sheet_layout,
                container, false);
        //title = "Hi";
        TextView text = (TextView) window.findViewById(R.id.eventTitle);
        text.setText(title);
        return window;
    }

}
