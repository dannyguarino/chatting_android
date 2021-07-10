package com.example.chatting.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NotificationCompat;

import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatting.Adapter.MainRecyclerAdapter;
import com.example.chatting.Application.App;
import com.example.chatting.DAO.MessageDAO;
import com.example.chatting.DAO.UserDAO;
import com.example.chatting.Model.ItemMain;
import com.example.chatting.Model.Message;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.ImageConvert;
import com.example.chatting.Provider.SharedPreferenceProvider;
import com.example.chatting.R;
import com.example.chatting.Service.NotificationService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    NotificationManagerCompat notificationManagerCompat;

    List<User> onlines;
    List<User> chats;
    List<Message> lastMessages;
    List<ItemMain> itemMains;
    MainRecyclerAdapter mainRecyclerAdapter;

    User user;

    RecyclerView rc_main;
    SwipeRefreshLayout swipe_main;
    BottomNavigationView bottom_nav;
    Intent intent;
    ImageView img_avatar;
    TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getModel();
        getView();
        setView();
        setOnClick();
        //Start service notification of message
        startService(new Intent(this, NotificationService.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getModel(){

        user = (User)SharedPreferenceProvider.getInstance(this).get("user");
        onlines = new ArrayList<User>();
        chats = new ArrayList<User>();
        itemMains = new ArrayList<ItemMain>();
        lastMessages = new ArrayList<Message>();
    }

    public void getView(){
        rc_main = findViewById(R.id.rc_main);
        swipe_main = findViewById(R.id.swipe_main);
        bottom_nav = findViewById(R.id.bottom_nav);
        img_avatar = findViewById(R.id.img_avatar);
        tv_name = findViewById(R.id.tv_name);
    }

    public void setView(){

        bottom_nav.setSelectedItemId(R.id.chats);
        ImageConvert.setUrlToImageView(img_avatar, user.getAvatar());
        tv_name.setText(user.getName());

//        loadData();

        mainRecyclerAdapter = new MainRecyclerAdapter(itemMains, user);
        rc_main.setLayoutManager(new LinearLayoutManager(this));
        rc_main.setItemAnimator(new DefaultItemAnimator());
        rc_main.setAdapter(mainRecyclerAdapter);

        loadDataChats();
    }

    public void setOnClick(){
//        rc_main.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rc_main.getLayoutManager();
//                int total = linearLayoutManager.getItemCount();
//                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//                if (total < lastVisible + 3){
//                    if (!isLoading){
//                        isLoading = true;
//                        loadDataChats();
//                    }
//
//                }
//            }
//        });

        bottom_nav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        img_avatar.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_avatar:
                intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void loadData(){
        itemMains.add(new ItemMain(onlines, ItemMain.ItemType.ONLINE_ITEM));
    }

    public void loadDataChats(){
        MessageDAO.getInstance().gets(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()){
                    Message message = data.getValue(Message.class);
                    UserDAO.getInstance().get(message.getFriendId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data: snapshot.getChildren()) {
                                User user = data.getValue(User.class);
                                int position = getPosition(chats, user);
                                if (position > -1) {
                                    chats.remove(position);
                                    lastMessages.remove(position);
                                }
                                chats.add(0, user);
                                lastMessages.add(0, message);
                            }
                            updateDataOnline(chats, lastMessages);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

//                Collections.reverse(chats);
//                Collections.reverse(lastMessages);
//                updateDataOnline(chats, lastMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    int getPosition(List<User> users, User user){
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).getId().equals(user.getId()))
                return i;
        }
        return -1;
    }

    public void updateDataOnline(List<User> users, List<Message> messages) {
        itemMains.clear();
        loadData();
        List<ItemMain> items = new ArrayList<ItemMain>();
        for (int i = 0; i < users.size(); i++){
            items.add(new ItemMain(users.get(i), messages.get(i), ItemMain.ItemType.CHAT_ITEM));
        }
        itemMains.addAll(items);
        mainRecyclerAdapter.setItems(itemMains);
        mainRecyclerAdapter.notifyDataSetChanged();
    }

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.chats:
                    return false;
                case R.id.people:
                    intent = new Intent(getApplicationContext(), PeopleActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        }
    };

}