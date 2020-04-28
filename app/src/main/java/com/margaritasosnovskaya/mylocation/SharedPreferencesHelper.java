package com.margaritasosnovskaya.mylocation;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.margaritasosnovskaya.mylocation.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SharedPreferencesHelper {

    public static final String  SHARED_PREF_NAME = "SHARED_PREF_NAME";
    public static final String USERS_KEY = "USERS_KEY";
    public static final Type USERS_TYPE = new TypeToken<List<User>>(){}.getType();

    private SharedPreferences mSharedPreferences;
    private Gson mGson = new Gson();

    public SharedPreferencesHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }

    public List<User> getUsers(){
        List<User> users = mGson.fromJson(mSharedPreferences.getString(USERS_KEY, USERS_KEY), USERS_TYPE);
        return users == null ?  new ArrayList<User>() : users;
    }

    public  boolean addUser(User user){
        List<User> users = getUsers();
        for(User u:users){
            if(u.getEmail().equalsIgnoreCase(user.getEmail())){
                return false;
            }
        }
        users.add(user);
        mSharedPreferences.edit().putString(USERS_KEY,mGson.toJson(users,USERS_TYPE)).apply();
        return true;
    }
}
