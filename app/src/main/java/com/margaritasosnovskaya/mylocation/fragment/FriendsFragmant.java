package com.margaritasosnovskaya.mylocation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.margaritasosnovskaya.mylocation.R;


public class FriendsFragmant extends Fragment {

    private static int LAYOUT = R.layout.fragment_friends;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(LAYOUT,container,false);
    }
}
