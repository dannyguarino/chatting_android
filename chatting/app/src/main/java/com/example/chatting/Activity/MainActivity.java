package com.example.chatting.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.chatting.Adapter.MainRecyclerAdapter;
import com.example.chatting.Model.ItemMain;
import com.example.chatting.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    List<String> onlines;
    List<String> chats;
    List<ItemMain> itemMains;
    MainRecyclerAdapter mainRecyclerAdapter;

    RecyclerView rc_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getModel();
        getView();
        setView();
        setOnClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getModel(){

        onlines = new ArrayList<String>();
        chats = new ArrayList<String>();
        itemMains = new ArrayList<ItemMain>();
    }

    public void getView(){
        rc_main = findViewById(R.id.rc_main);
    }

    public void setView(){
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        onlines.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");
        chats.add("1");

        itemMains.add(new ItemMain(onlines, ItemMain.ItemType.ONLINE_ITEM));
        for (int i = 0; i < chats.size(); i++){
            itemMains.add(new ItemMain(chats.get(i), ItemMain.ItemType.CHAT_ITEM));
        }

        System.out.println(itemMains.size());

        mainRecyclerAdapter = new MainRecyclerAdapter(itemMains);
        rc_main.setLayoutManager(new LinearLayoutManager(this));
        rc_main.setItemAnimator(new DefaultItemAnimator());
        rc_main.setAdapter(mainRecyclerAdapter);
    }

    public void setOnClick(){

    }

    public void onClick(View view){

    }
}