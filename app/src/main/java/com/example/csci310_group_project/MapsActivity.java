package com.example.csci310_group_project;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.csci310_group_project.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Haunt_Massive = new LatLng(34.09960, -118.32753);
        LatLng Fright_Night2K22 = new LatLng(34.00791,-118.10745);
        LatLng ShaneShane_Christmas_Concert_2022 = new LatLng(33.92312,-118.13908);
        LatLng The_Art_of_The_Owl_House_Exhibition = new LatLng(34.09053,-118.13460);
        LatLng FNth_Annual_Dia_de_los_Muertos_Exhibition = new LatLng(34.04966, -118.21131);
        LatLng Adaptive_Sports_Festival_2022 = new LatLng(34.16766,-118.16957);
        LatLng TOTT_DREAM_All_American_Bowl = new LatLng(33.95836,-118.36129);
        LatLng Atheon_Race = new LatLng(34.01134, -118.46846);
        LatLng Chocolate_Sundaes_Comedy = new LatLng(34.10727, -118.36889);
        LatLng Faded_Comedy_22 = new LatLng(33.95304, -118.30943);
        LatLng Abbot_Kidding_A_Comedy_Show_in_Venice = new LatLng(33.99416, -118.46371);
        LatLng Chocolate_and_Art_Show = new LatLng(34.02984, -118.24034);
        LatLng The_Setup_Comedy = new LatLng(34.01932, -118.39349);
        LatLng Sunset_Vibes_Silent_Disco = new LatLng(33.86479, -118.39719);
        LatLng Joachim_Horsley_Caribbean_Nocturnes_In_Concert= new LatLng(34.09960, -118.32753);
        LatLng Jazz_Eclectic_Night= new LatLng(34.07100, -118.37618);
        LatLng An_Evening_of_World_Music_and_Jazz= new LatLng(33.95507,-118.40200);
        LatLng Leslie_Baker_Ready_Now_LIVE = new LatLng(34.13410, -118.20533);
        LatLng MoreLuv_RnB = new LatLng(34.04303, -118.25227);
        LatLng AfroVibesLA_Sunday= new LatLng(34.09960, -118.32753);

        mMap.addMarker(new MarkerOptions().position(Haunt_Massive).title("Haunt Massive"));
        mMap.addMarker(new MarkerOptions().position(Fright_Night2K22).title("Fright Night2K22"));
        mMap.addMarker(new MarkerOptions().position(ShaneShane_Christmas_Concert_2022).title("Shane & Shane Christmas Concert 2022"));
        mMap.addMarker(new MarkerOptions().position(The_Art_of_The_Owl_House_Exhibition).title("The Art of The Owl House Exhibition"));
        mMap.addMarker(new MarkerOptions().position(FNth_Annual_Dia_de_los_Muertos_Exhibition).title("59th Annual Dia de los Muertos Exhibition"));
        mMap.addMarker(new MarkerOptions().position(Adaptive_Sports_Festival_2022).title("Adaptive Sports Festival 2022"));
        mMap.addMarker(new MarkerOptions().position(TOTT_DREAM_All_American_Bowl).title("2022 DREAM All American Bowl"));
        mMap.addMarker(new MarkerOptions().position(Atheon_Race).title("Atheon Race"));
        mMap.addMarker(new MarkerOptions().position(Chocolate_Sundaes_Comedy).title("Chocolate Sundaes Comedy"));
        mMap.addMarker(new MarkerOptions().position(Faded_Comedy_22).title("Faded Comedy 22"));
        mMap.addMarker(new MarkerOptions().position(Abbot_Kidding_A_Comedy_Show_in_Venice).title("Abbot Kidding: A Comedy Show in Venice"));
        mMap.addMarker(new MarkerOptions().position(Chocolate_and_Art_Show).title("Chocolate and Art Show"));
        mMap.addMarker(new MarkerOptions().position(The_Setup_Comedy).title("The Setup Comedy"));
        mMap.addMarker(new MarkerOptions().position(Sunset_Vibes_Silent_Disco).title("Sunset Vibes Silent Disco"));
        mMap.addMarker(new MarkerOptions().position(Joachim_Horsley_Caribbean_Nocturnes_In_Concert).title("Joachim Horsley: Caribbean Nocturnes In Concert"));
        mMap.addMarker(new MarkerOptions().position(Jazz_Eclectic_Night).title("Jazz Eclectic Night"));
        mMap.addMarker(new MarkerOptions().position(An_Evening_of_World_Music_and_Jazz).title("An Evening of World Music and Jazz"));
        mMap.addMarker(new MarkerOptions().position(Leslie_Baker_Ready_Now_LIVE).title("Leslie Baker & Ready Now LIVE"));
        mMap.addMarker(new MarkerOptions().position(MoreLuv_RnB).title("MoreLuv: RnB"));
        mMap.addMarker(new MarkerOptions().position(AfroVibesLA_Sunday).title("AfroVibesLA Sunday"));


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}