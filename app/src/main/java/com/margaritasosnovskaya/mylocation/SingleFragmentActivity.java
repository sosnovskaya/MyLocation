package com.margaritasosnovskaya.mylocation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.margaritasosnovskaya.mylocation.R.*;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_single_fragment);

        if(savedInstanceState == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(id.single_frag_container, getFragment())
                    .commit();
        }
    }
    protected abstract Fragment getFragment();
}
