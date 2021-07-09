package com.example.chatting.Fragment;

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

import com.example.chatting.Adapter.FriendRecyclerAdapter;
import com.example.chatting.DAO.FriendDAO;
import com.example.chatting.DAO.UserDAO;
import com.example.chatting.Model.Friend;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.SharedPreferenceProvider;
import com.example.chatting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
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

    //Model
    List<User> users;
    User user;
    FriendRecyclerAdapter friendRecyclerAdapter;
    List<Friend> friends;

    boolean isLoading = false;

    RecyclerView rc_friend;
    SwipeRefreshLayout swipe_friend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_friend, container, false);

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
        rc_friend = view.findViewById(R.id.rc_friend);
        swipe_friend = view.findViewById(R.id.swipe_friend);
    }

    public void setView(View view){
//
//        FriendDAO.getInstance().add(new Friend(9, 1));
//        FriendDAO.getInstance().add(new Friend(1, 9));
//        FriendDAO.getInstance().add(new Friend(9, 3));
//        FriendDAO.getInstance().add(new Friend(3, 9));
//        FriendDAO.getInstance().add(new Friend(9, 12));
//        FriendDAO.getInstance().add(new Friend(12, 9));

        friendRecyclerAdapter = new FriendRecyclerAdapter(users, user);
        rc_friend.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rc_friend.setItemAnimator(new DefaultItemAnimator());
        rc_friend.setAdapter(friendRecyclerAdapter);
        loadData();
//        rc_friend.addOnScrollListener(onScrollListener);
    }

    private void loadData() {
//        swipe_friend.setRefreshing(true);
        FriendDAO.getInstance().gets(user).addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            friends = new ArrayList<>();
            users = new ArrayList<>();
            for (DataSnapshot data: snapshot.getChildren()){
                Friend friend = data.getValue(Friend.class);
                friends.add(friend);
            }
            FriendDAO.getInstance().getsFriendId(user).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data: snapshot.getChildren()){
                        Friend friend = data.getValue(Friend.class);
                        friends.add(friend);
                    }

                    for (Friend friend: friends){
                        if (friend.isState()){
                            if (user.getId().equals(friend.getUserId())) {
                                UserDAO.getInstance().get(friend.getFriendId()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot data : snapshot.getChildren()) {
                                            User user = data.getValue(User.class);
                                            users.add(user);
                                            friendRecyclerAdapter.setUsers(users);
                                            friendRecyclerAdapter.notifyDataSetChanged();
//                                isLoading = false;
//                                swipe_friend.setRefreshing(false);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
//                            swipe_friend.setRefreshing(false);
                                    }
                                });
                            }else{
                                UserDAO.getInstance().get(friend.getUserId()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot data : snapshot.getChildren()) {
                                            User user = data.getValue(User.class);
                                            users.add(user);
                                            friendRecyclerAdapter.setUsers(users);
                                            friendRecyclerAdapter.notifyDataSetChanged();
//                                isLoading = false;
//                                swipe_friend.setRefreshing(false);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
//                            swipe_friend.setRefreshing(false);
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            int totalItem = linearLayoutManager.getItemCount();
            int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            if (totalItem < lastVisible + 3){
                isLoading = true;
                loadData();
            }
        }
    };

    public interface FirebaseCallBack{
        void onCallBack(List<User> users);
    }

}