package com.example.chatting.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.chatting.Adapter.PeopleAdapter;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.SharedPreferenceProvider;
import com.example.chatting.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class PeopleActivity extends AppCompatActivity implements View.OnClickListener{

    User user;

    TabLayout tab_people;
    ViewPager vp_people;
    BottomNavigationView bottom_nav;
    Intent intent;

    //Adapter
    PeopleAdapter peopleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        getModel();
        getView();
        setView();
        setOnClick();
    }

    public void getModel(){
        user = (User) SharedPreferenceProvider.getInstance(this).get("user");
    }

    public void getView(){
        tab_people = findViewById(R.id.tab_people);
        vp_people = findViewById(R.id.vp_people);
        bottom_nav = findViewById(R.id.bottom_nav);
    }

    public void setView(){
        bottom_nav.setSelectedItemId(R.id.people);

        //Adapter
        peopleAdapter = new PeopleAdapter(getSupportFragmentManager(), tab_people.getTabCount());
        vp_people.setAdapter(peopleAdapter);
        vp_people.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_people));
    }

    public void setOnClick(){
        tab_people.addOnTabSelectedListener(onTabSelectedListener);
        bottom_nav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    public void onClick(View view){

    }

    TabLayout.OnTabSelectedListener onTabSelectedListener =  new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            vp_people.setCurrentItem(tab.getPosition());
            if (tab.getPosition() == 0 || tab.getPosition() == 1){
                peopleAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.people:
                    return false;
                case R.id.chats:
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        }
    };


}