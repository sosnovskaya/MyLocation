package com.margaritasosnovskaya.mylocation.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.margaritasosnovskaya.mylocation.R;
import com.margaritasosnovskaya.mylocation.services.LocationService;


public class ShowMapsFragment extends Fragment implements OnMapReadyCallback {

    private static int LAYOUT = R.layout.fragment_maps_location;
    private View view;
    private GoogleMap mMap;
    private Marker marker;
    private Location mLocation;
    private MyReceiver receiver;

    public static ShowMapsFragment newInstance(){
        Bundle args  = new Bundle();
        ShowMapsFragment fragment = new ShowMapsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationService.EXTRA_LOCATION);
            mLocation = location;
            if (location != null && marker != null) {
                LatLng mPosition = new LatLng(location.getLatitude(),location.getLongitude());
                marker.setPosition(mPosition);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPosition,15));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);
        Bundle args = getArguments();
        receiver = new MyReceiver();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_location);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter(LocationService.ACTION_BROADCAST));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        super.onPause();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(37.42, -122.08);
            marker = mMap.addMarker(new MarkerOptions().position(sydney).title("I'm here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
    }
}
