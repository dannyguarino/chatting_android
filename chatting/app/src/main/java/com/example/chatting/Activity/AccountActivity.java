package com.example.chatting.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chatting.Model.User;
import com.example.chatting.Provider.SharedPreferenceProvider;
import com.example.chatting.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener{

    private GoogleApiClient mGoogleApiClient;

    User user;
    Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getModel();
        getView();
        setView();
        setOnClick();
        configGoogleAccount();
    }

    public void getModel(){
        user = (User) SharedPreferenceProvider.getInstance(this).get("user");
    }

    public void getView(){
        btn_logout = findViewById(R.id.btn_logout);
    }

    public void setView(){

    }

    public void setOnClick(){
        btn_logout.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    void configGoogleAccount(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
    }

    public void logout(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        SharedPreferenceProvider.getInstance(AccountActivity.this).set("user", null);
                        Intent i =new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                    }
                });
    }
}