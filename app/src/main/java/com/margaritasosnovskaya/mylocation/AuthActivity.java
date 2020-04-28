package com.margaritasosnovskaya.mylocation;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.margaritasosnovskaya.mylocation.fragment.AuthFragment;

public class AuthActivity extends SingleFragmentActivity {

    private final String TAG = AuthActivity.class.getSimpleName();

    @Override
    protected Fragment getFragment() {
        Log.i(TAG, "getFragment: Return authFragment");
        return AuthFragment.newInstance();
    }
}
