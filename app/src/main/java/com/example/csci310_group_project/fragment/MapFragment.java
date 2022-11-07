package com.example.csci310_group_project.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import com.example.csci310_group_project.BottomSheetDialog;
import com.example.csci310_group_project.EventDetailActivity;
import com.example.csci310_group_project.customInfoWindowAdapter;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.csci310_group_project.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

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

import java.security.Permission;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 999;
    GoogleApiAvailability googleAPI;
    SupportMapFragment mapFragment;

    Boolean initialized = false;
    private String user = "";

    public void setUser(String username){
        this.user = username;
    }


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        checkPlayServices();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        mapFragment.getMapAsync(this);

        return view;
    }


    private boolean checkPlayServices() {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
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

    @SuppressLint("MissingPermission")
    private ActivityResultLauncher<String> requestPermissionLauncher =
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
 /*
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("MapPermission3", "Here");
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("MapPermission4", "Here");
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
    }

  */

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

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
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        if(!initialized) {
            LatLng Haunt_Massive = new LatLng(34.09960, -118.32703);
            LatLng Fright_Night2K22 = new LatLng(34.00791, -118.10745);
            LatLng ShaneShane_Christmas_Concert_2022 = new LatLng(33.92312, -118.13908);
            LatLng The_Art_of_The_Owl_House_Exhibition = new LatLng(34.09053, -118.13460);
            LatLng FNth_Annual_Dia_de_los_Muertos_Exhibition = new LatLng(34.04966, -118.21131);
            LatLng Adaptive_Sports_Festival_2022 = new LatLng(34.16766, -118.16957);
            LatLng TOTT_DREAM_All_American_Bowl = new LatLng(33.95836, -118.36129);
            LatLng Atheon_Race = new LatLng(34.01134, -118.46846);
            LatLng Chocolate_Sundaes_Comedy = new LatLng(34.10727, -118.36889);
            LatLng Faded_Comedy_22 = new LatLng(33.95304, -118.30943);
            LatLng Abbot_Kidding_A_Comedy_Show_in_Venice = new LatLng(33.99416, -118.46371);
            LatLng Chocolate_and_Art_Show = new LatLng(34.02984, -118.24034);
            LatLng The_Setup_Comedy = new LatLng(34.01932, -118.39349);
            LatLng Sunset_Vibes_Silent_Disco = new LatLng(33.86479, -118.39719);
            LatLng Joachim_Horsley_Caribbean_Nocturnes_In_Concert = new LatLng(34.08960, -118.33753);
            LatLng Jazz_Eclectic_Night = new LatLng(34.07100, -118.37618);
            LatLng An_Evening_of_World_Music_and_Jazz = new LatLng(33.95507, -118.40200);
            LatLng Leslie_Baker_Ready_Now_LIVE = new LatLng(34.13410, -118.20533);
            LatLng MoreLuv_RnB = new LatLng(34.04303, -118.25227);
            LatLng AfroVibesLA_Sunday = new LatLng(34.07960, -118.30853);

            mMap.addMarker(new MarkerOptions().position(Haunt_Massive).title("Haunt Massive")); // done
            mMap.addMarker(new MarkerOptions().position(Fright_Night2K22).title("Fright Night 2K22")); // done
            mMap.addMarker(new MarkerOptions().position(ShaneShane_Christmas_Concert_2022).title("Shane & Shane Christmas Concert 2022")); // done
            mMap.addMarker(new MarkerOptions().position(The_Art_of_The_Owl_House_Exhibition).title("The Art of The Owl House Exhibition ")); // done
            mMap.addMarker(new MarkerOptions().position(FNth_Annual_Dia_de_los_Muertos_Exhibition).title("49th Annual Dia de los Muertos Exhibition")); // done
            mMap.addMarker(new MarkerOptions().position(Adaptive_Sports_Festival_2022).title("Adaptive Sports Festival 2022")); // done
            mMap.addMarker(new MarkerOptions().position(TOTT_DREAM_All_American_Bowl).title("2023 DREAM All-American Bowl")); // done
            mMap.addMarker(new MarkerOptions().position(Atheon_Race).title("Atheon Race")); // done
            mMap.addMarker(new MarkerOptions().position(Chocolate_Sundaes_Comedy).title("Chocolate Sundaes Comedy @ The Laugh Factory Hollywood")); // done
            mMap.addMarker(new MarkerOptions().position(Faded_Comedy_22).title("Faded Comedy")); // done
            mMap.addMarker(new MarkerOptions().position(Abbot_Kidding_A_Comedy_Show_in_Venice).title("Abbot Kidding: A Comedy Show in Venice")); // done
            mMap.addMarker(new MarkerOptions().position(Chocolate_and_Art_Show).title("Chocolate and Art Show")); // done
            mMap.addMarker(new MarkerOptions().position(The_Setup_Comedy).title("The Setup Presents: Citizen Public Market Comedy Night")); // done
            mMap.addMarker(new MarkerOptions().position(Sunset_Vibes_Silent_Disco).title("Sunset Vibes Silent Disco")); // done
            mMap.addMarker(new MarkerOptions().position(Joachim_Horsley_Caribbean_Nocturnes_In_Concert).title("Joachim Horsley: Caribbean Nocturnes In Concert"));
            mMap.addMarker(new MarkerOptions().position(Jazz_Eclectic_Night).title("Jazz Eclectic Night")); // done
            mMap.addMarker(new MarkerOptions().position(An_Evening_of_World_Music_and_Jazz).title("An Evening of World Music and Jazz")); // done
            mMap.addMarker(new MarkerOptions().position(Leslie_Baker_Ready_Now_LIVE).title("Leslie Baker & Ready Now LIVE")); // done
            mMap.addMarker(new MarkerOptions().position(MoreLuv_RnB).title("MoreLuv: RnB")); // done
            mMap.addMarker(new MarkerOptions().position(AfroVibesLA_Sunday).title("AfroVibesLA Sunday"));

            mMap.setOnInfoWindowClickListener(this);
            mMap.setInfoWindowAdapter(new customInfoWindowAdapter(getActivity()));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    BottomSheetDialog dialog = new BottomSheetDialog();
                    BottomSheetDialog.title = marker.getTitle();
                    dialog.setUser(user);
                    dialog.show(getActivity().getSupportFragmentManager(), "Sample dialog");

                    return true;
                }
            });
            //mMap.setOnMarkerDragListener(this);
        }

        //move map camera
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("EVENT_INDEX", marker.getTitle());
        intent.putExtra("event_name", marker.getTitle());
        intent.putExtra("user", user);
        startActivity(intent);

    }
}



