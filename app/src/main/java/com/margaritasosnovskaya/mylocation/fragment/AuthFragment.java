package com.margaritasosnovskaya.mylocation.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.margaritasosnovskaya.mylocation.AuthActivity;
import com.margaritasosnovskaya.mylocation.MainActivity;
import com.margaritasosnovskaya.mylocation.R;
import com.margaritasosnovskaya.mylocation.model.User;

public class AuthFragment extends Fragment {

    private static int LAYOUT = R.layout.fragment_auth;
    private static final String TAG = AuthActivity.class.getSimpleName();

    private EditText mEmail;
    private EditText mPassword;
    private Button mSignIn;
    private Button mLogIn;


    public static AuthFragment newInstance() {
        Bundle args = new Bundle();

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: AuthFragment loaded!");
        View view = inflater.inflate(LAYOUT,container,false);

        mEmail = view.findViewById(R.id.etEmail);
        mPassword = view.findViewById(R.id.etPassword);
        mSignIn = view.findViewById(R.id.btnSignIn);
        mLogIn = view.findViewById(R.id.btnLogIn);


        mSignIn.setOnClickListener(mSignInClickListener);
        mLogIn.setOnClickListener(mLogInClickListener);

        return view;
    }

    private View.OnClickListener mSignInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isEmailValid() && isPasswordValid()){
                Intent mainIntent =
                        new Intent(getActivity(), MainActivity.class);
                mainIntent.putExtra(MainActivity.USER_KEY,
                        new User(mEmail.getText().toString(),"User_name",mPassword.getText().toString()));
                Log.i(TAG, "onClick: go to MainActivity");
                startActivity(mainIntent);
                //добавить проверку,что такой пользователь есть в базе,когда будет база(
            }
            else{
                showMessageSignIn(R.string.signIn_input_error);
            }
        }
    };

    private View.OnClickListener mLogInClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.single_frag_container, RegistrationFragment.newInstance())
                    .commit();
        }
    };

    private void showMessageSignIn(@StringRes int string){
        Toast.makeText(getActivity(),string,Toast.LENGTH_LONG).show();
    }


    private boolean isEmailValid(){
        return !TextUtils.isEmpty(mEmail.getText())
                && Patterns.EMAIL_ADDRESS.matcher(mEmail.getText()).matches();
    }

    private boolean isPasswordValid(){
        return !TextUtils.isEmpty(mPassword.getText());
        //добавить совпадение пароля в базе
    }
}
