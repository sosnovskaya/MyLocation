package ru.margaritasosnovskaya.mylocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;


import ru.margaritasosnovskaya.mylocation.R;
import ru.margaritasosnovskaya.mylocation.fragment.FriendsFragmant;
import ru.margaritasosnovskaya.mylocation.fragment.LocationFragment;
import ru.margaritasosnovskaya.mylocation.fragment.PlacesFragmant;
import ru.margaritasosnovskaya.mylocation.fragment.SettingsFragmant;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String EMAIL_KEY = "EMAIL_KEY";
    public static String PASSWORD_KEY = "PASSWORD_KEY";

    private Toolbar toolbar;
    private DrawerLayout drawer;

    private AppCompatImageView mPhoto;
    private NavigationView navigationView;
    private TextView userEmail;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhoto = findViewById(R.id.profilePhoto);
        userEmail = findViewById(R.id.user_email);

        navigationView = findViewById(R.id.navigation_view);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.user_email);
        currentUserEmail = getIntent().getStringExtra(EMAIL_KEY);
        if(currentUserEmail != null && userEmail != null)
            userEmail.setText(currentUserEmail);


        initToolBar();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    LocationFragment.newInstance()).commit();
            navigationView.setCheckedItem(R.id.nav_location);
        }
    }



    private View.OnClickListener mPhotoOncLickLictener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

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
