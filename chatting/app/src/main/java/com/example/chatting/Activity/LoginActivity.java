package com.example.chatting.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatting.DAO.UserDAO;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.SharedPreferenceProvider;
import com.example.chatting.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    User user;

    String email = "tranan2502@gmail.com";

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        getModel();

        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void getModel(){
        UserDAO.getInstance().getUserData(new UserDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(List<User> users) {
                user = users.get(0);
                SharedPreferenceProvider.getInstance(getApplicationContext()).set("user", user);
            }
        }, email);
    }

    public void onClick(View view){

    }
}