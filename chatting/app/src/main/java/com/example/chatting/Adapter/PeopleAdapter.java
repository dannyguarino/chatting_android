package com.example.chatting.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatting.Fragment.CommunityFragment;
import com.example.chatting.Fragment.FriendFragment;

public class PeopleAdapter extends FragmentPagerAdapter {

    int tabCount = 0;

    public PeopleAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm);
        tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0: return new FriendFragment();
           case 1: return new CommunityFragment();
           default:return null;
       }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
