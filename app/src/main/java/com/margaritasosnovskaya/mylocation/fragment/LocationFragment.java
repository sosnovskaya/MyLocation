package com.margaritasosnovskaya.mylocation.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.margaritasosnovskaya.mylocation.R;

import java.util.Date;


import static android.content.Context.LOCATION_SERVICE;

public class LocationFragment extends Fragment {

    private static final int PERMISSION_ACCESS_FINE_LOCATION = 99;

    private static int LAYOUT = R.layout.fragment_location;
    private View view;

    private TextView tvEnabledGPS;
    private TextView tvStatusGPS;
    private TextView tvLocationGPS;
    private TextView tvEnabledNet;
    private TextView tvStatusNet;
    private TextView tvLocationNet;

    private LocationManager locationManager;

    public static LocationFragment newInstance(){
        Bundle args  = new Bundle();
        LocationFragment fragment = new LocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);
        Bundle args = getArguments();

        tvEnabledGPS = view.findViewById(R.id.tvEnabledGPS);
        tvStatusGPS =  view.findViewById(R.id.tvStatusGPS);
        tvLocationGPS =  view.findViewById(R.id.tvLocationGPS);
        tvEnabledNet =  view.findViewById(R.id.tvEnabledNet);
        tvStatusNet =  view.findViewById(R.id.tvStatusNet);
        tvLocationNet = view.findViewById(R.id.tvLocationNet);

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPerms();    }


    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000 * 10, 10, locationListener);

        checkEnabled();
    }

    private void requestPerms() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(getActivity(), "Need your location!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }


    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }


        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                tvStatusGPS.setText("Status: " + status); }
            else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                tvStatusNet.setText("Status: " + status);
            }
        }
    };


    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            tvLocationGPS.setText(formatLocation(location));
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            tvLocationNet.setText(formatLocation(location));
        }
    }

    private void checkEnabled() {
        tvEnabledGPS.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
        tvEnabledNet.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }


}
