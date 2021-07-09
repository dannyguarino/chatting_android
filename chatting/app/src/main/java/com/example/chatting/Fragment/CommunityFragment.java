package com.example.chatting.Fragment;

import android.icu.util.Freezable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatting.Adapter.CommunityRecyclerAdapter;
import com.example.chatting.Adapter.FriendRecyclerAdapter;
import com.example.chatting.DAO.FriendDAO;
import com.example.chatting.DAO.UserDAO;
import com.example.chatting.Model.Friend;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.SharedPreferenceProvider;
import com.example.chatting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    User user;
    List<User> users;
    List<Friend> friends;

    RecyclerView rc_community;
    SwipeRefreshLayout swipe_community;

    CommunityRecyclerAdapter communityRecyclerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_community, container, false);

        getModel(view);
        getViewFragment(view);
        setView(view);

        return view;
    }

    public void getModel(View view){
        user = (User) SharedPreferenceProvider.getInstance(view.getContext()).get("user");
        users = new ArrayList<>();
    }

    public void getViewFragment(View view){
        rc_community = view.findViewById(R.id.rc_community);
        swipe_community = view.findViewById(R.id.swipe_community);
    }

    public void setView(View view){

        communityRecyclerAdapter = new CommunityRecyclerAdapter(users, new ArrayList<>(), user);
        rc_community.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rc_community.setItemAnimator(new DefaultItemAnimator());
        rc_community.setAdapter(communityRecyclerAdapter);

        loadData();
    }

    private void loadData() {
        FriendDAO.getInstance().getFriendData(new FriendDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(List<Friend> mFriends) {
                friends = mFriends;
                UserDAO.getInstance().gets().addValueEventListener(valueEventListener);
            }
        }, user);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Friend> mFriends = new ArrayList<>();
            users = new ArrayList<>();
            for (DataSnapshot data: snapshot.getChildren()){
                User user = data.getValue(User.class);
                int check = checkUser(friends, user);
                Friend friend = null;
                if (check != -1){
                    if (check > -1){
                        friend = friends.get(check);
                    }
                    mFriends.add(friend);
                    users.add(user);
                }
            }
            communityRecyclerAdapter.setUsers(users);
            communityRecyclerAdapter.setFriends(mFriends);
            communityRecyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public int checkUser(List<Friend> friends, User mUser){
        if (user.getId().equals(mUser.getId()))
            return -1;

        for (int i = 0; i < friends.size(); i++){
            Friend friend = friends.get(i);
            if (friend.getFriendId().equals(mUser.getId()) || friend.getUserId().equals(mUser.getId())){
                if (friend.isState())
                    //Friended
                    return -1;
                return i;
            }
        }
        return -2;
    }
}