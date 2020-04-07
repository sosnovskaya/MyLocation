package ru.margaritasosnovskaya.mylocation;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity {

    private static int LAYOUT = R.layout.activity_auth;

    private EditText mEmail;
    private EditText mPassword;
    private Button mSignIn;
    private Button mLogIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mEmail = findViewById(R.id.etEmail);
        mPassword =findViewById(R.id.etPassword);
        mSignIn = findViewById(R.id.btnSignIn);
        mLogIn = findViewById(R.id.btnLogIn);


        mSignIn.setOnClickListener(mSignInClickListener);
        mLogIn.setOnClickListener(mLogInClickListener);
    }

    private View.OnClickListener mSignInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isEmailValid() && isPasswordValid()){
                Intent startMainIntent =
                        new Intent(AuthActivity.this, MainActivity.class);
                startMainIntent.putExtra(MainActivity.EMAIL_KEY, mEmail.getText().toString());
                startActivity(startMainIntent);
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

        }
    };

    private void showMessageSignIn(@StringRes int string){
        Toast.makeText(this,string,Toast.LENGTH_LONG).show();
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
