package ru.margaritasosnovskaya.mylocation;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import ru.margaritasosnovskaya.mylocation.model.User;

public class RegistrationFragment extends Fragment {

    private static int LAYOUT = R.layout.fragment_registartion;

    private EditText mEmail;
    private EditText mLogin;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private Button mRegistration;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    public static RegistrationFragment newInstance() {
        Bundle args = new Bundle();

        RegistrationFragment fragment = new RegistrationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener mOnRegistrationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isInputValid()){
                boolean isAdded = mSharedPreferencesHelper.addUser(new User(
                        mEmail.getText().toString(),
                        mLogin.getText().toString(),
                        mPassword.getText().toString())
                );

                if(isAdded){
                    showMessage(R.string.login_register_success);
                }
                else{
                    showMessage(R.string.login_register_failed);
                }
            }
            else{
                showMessage(R.string.input_error);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT,container,false);

        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        mEmail = view.findViewById(R.id.etEmail);
        mLogin = view.findViewById(R.id.etLogin);
        mPassword = view.findViewById(R.id.etPassword);
        mPasswordAgain = view.findViewById(R.id.etPasswordAgain);
        mRegistration = view.findViewById(R.id.btnRegistration);

        mRegistration.setOnClickListener(mOnRegistrationClickListener);

        return view;
    }


    public boolean isInputValid(){
        String email = mEmail.getText().toString();
        if(isEmailValid(email) && isPasswordValid()){
            return true;
        }
        return false;
    }

    private boolean isEmailValid(String email){
        return !TextUtils.isEmpty(email)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        //проверить,что такого нет в базе
    }

    private boolean isPasswordValid(){
        String password = mPassword.getText().toString();
        String passwordAgain = mPasswordAgain.getText().toString();

        return password.equals(passwordAgain)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(passwordAgain);
    }

    private void showMessage(@StringRes int string){
        Toast.makeText(getActivity(),string,Toast.LENGTH_LONG).show();
    }

}
