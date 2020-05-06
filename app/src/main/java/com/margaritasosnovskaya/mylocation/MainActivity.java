package com.margaritasosnovskaya.mylocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;


import com.google.android.material.snackbar.Snackbar;
import com.margaritasosnovskaya.mylocation.fragment.FriendsFragmant;
import com.margaritasosnovskaya.mylocation.fragment.LocationFragment;
import com.margaritasosnovskaya.mylocation.fragment.PlacesFragmant;
import com.margaritasosnovskaya.mylocation.fragment.SettingsFragmant;
import com.margaritasosnovskaya.mylocation.fragment.ShowMapsFragment;
import com.margaritasosnovskaya.mylocation.model.User;
import com.margaritasosnovskaya.mylocation.services.LocationService;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static String USER_KEY = "EMAIL_KEY";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 34;

    private Toolbar toolbar;
    private DrawerLayout drawer;

    private AppCompatImageView mPhoto;
    private NavigationView navigationView;
    private TextView userEmail;
    private String currentUserEmail;
    private User user;

    private LocationService locationService;
    private boolean isBounded = false;
    private MyReceiver receiver;
    private Intent serviceIntent;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            locationService = binder.getService();
            isBounded = true;
            if (!checkLocationPermissions()) {
                requestPermissions();
            } else {
                locationService.requestLocationUpdates();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
            isBounded = false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: MainActivityCreated");
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.get(USER_KEY);

        receiver = new MyReceiver();

        mPhoto = findViewById(R.id.profilePhoto);
        navigationView = findViewById(R.id.navigation_view);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.user_email);
        userEmail.setText(user.getEmail());

        initToolBar();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(!checkLocationPermissions()){
            requestPermissions();
        }

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    LocationFragment.newInstance()).commit();
            navigationView.setCheckedItem(R.id.nav_location);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: BIND TO SERVICE AND NAINACTIVITY START");
        bindService(new Intent(this, LocationService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
//                new IntentFilter(LocationService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (isBounded) {
            unbindService(mServiceConnection);
            isBounded = false;
        }
        super.onStop();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationService.EXTRA_LOCATION);
            if (location != null) {
                Toast.makeText(MainActivity.this, getLocationText(location),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private boolean checkLocationPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();

//            new AlertDialog.Builder(this)
//                    .setMessage("Необъодим доступ к местоположению устройства")
//                    .setPositiveButton("Понято", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                    LOCATION_PERMISSIONS_REQUEST_CODE);
//                        }
//                    })
//                    .show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
             if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationService.requestLocationUpdates();
             } else {
                Snackbar.make(
                        findViewById(R.id.activity_main),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();

//                   new AlertDialog.Builder(this)
//                    .setMessage("Вы можете дать разрешение в настройках.")
//                    .setPositiveButton("Понято", null)
//                    .show();
            }
        }
    }

    private void initToolBar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
        //setSupportActionBar(toolbar);
    }

    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_location:{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        LocationFragment.newInstance()).commit();
                break;
            }
            case R.id.nav_map_location:{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        ShowMapsFragment.newInstance()).commit();
                break;
            }
            case R.id.nav_friends:{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FriendsFragmant()).commit();
                break;
            }
            case R.id.nav_places:{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PlacesFragmant()).commit();
                break;
            }
            case R.id.nav_settings:{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragmant()).commit();
                break;
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
