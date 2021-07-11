package com.example.chatting.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatting.DAO.UserDAO;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.SharedPreferenceProvider;
import com.example.chatting.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_login, btn_sign_up;
    EditText txt_name, txt_email, txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getView();
        setView();
        setOnClick();

    }

    public void getView(){
        btn_login = findViewById(R.id.btn_login);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        txt_name = findViewById(R.id.txt_name);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
    }

    public void setView(){

    }

    public void setOnClick(){
        btn_login.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sign_up:
                signUp();
                break;
        }
    }

    private void signUp() {
        String name = txt_name.getText().toString();
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();

        if (name.equals("") || email.equals("") || password.equals("")){
            Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
        }else{
            User user = new User(name, email, password);
            String id = UserDAO.getInstance().add(user);
            user.setId(id);
            Intent intent = new Intent(this, MainActivity.class);
            SharedPreferenceProvider.getInstance(getApplicationContext()).set("user", user);
            startActivity(intent);
        }
    }
}