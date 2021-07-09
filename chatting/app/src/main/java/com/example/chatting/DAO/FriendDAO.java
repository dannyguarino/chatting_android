package com.example.chatting.DAO;

import androidx.annotation.NonNull;

import com.example.chatting.Model.Friend;
import com.example.chatting.Model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendDAO {

    private static List<User> users;
    private static List<Friend> friends;

    private static final Object lock = new Object();
    private static FriendDAO instance;
    public static FriendDAO getInstance(){
        synchronized (lock){
            if (instance == null){
                instance = new FriendDAO();
            }
            return instance;
        }
    }

    public DatabaseReference databaseReference;
    public FirebaseDatabase db;

    public FriendDAO(){
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Friend.class.getSimpleName());
        users = new ArrayList<User>();
    }

    public Task<Void> add(Friend friend){
//        return databaseReference.push().setValue(user);
        String friendId = databaseReference.push().getKey();
        friend.setId(friendId);
        return databaseReference.child(String.valueOf(friend.getId())).setValue(friend);
    }

    public Query gets(User user){
        return databaseReference.orderByChild("userId").equalTo(user.getId());
    }

    public Query getsFriendId(User user){
        return databaseReference.orderByChild("friendId").equalTo(user.getId());
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

    public Task<Void> remove(Friend friend){

        return databaseReference.child(friend.getId()).removeValue();
    }

    public void getFriendData(FirebaseCallBack firebaseCallBack, User user){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 friends = new ArrayList<Friend>();
                for (DataSnapshot data: snapshot.getChildren()){
                    Friend friend = data.getValue(Friend.class);
                    friends.add(friend);
                }
                databaseReference.orderByChild("friendId").equalTo(user.getId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data: snapshot.getChildren()){
                            Friend friend = data.getValue(Friend.class);
                            friends.add(friend);
                        }
                        firebaseCallBack.onCallBack(friends);
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
        databaseReference.orderByChild("userId").equalTo(user.getId()).addValueEventListener(valueEventListener);
    }

    public Task<Void> update(Friend friend){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", friend.getId());
        hashMap.put("userId", friend.getUserId());
        hashMap.put("friendId", friend.getFriendId());
        hashMap.put("myself", friend.isMyself());
        hashMap.put("date", friend.getDate());
        hashMap.put("state", friend.isState());
        return databaseReference.child(friend.getId()).updateChildren(hashMap);
    }

    public interface FirebaseCallBack{
        void onCallBack(List<Friend> friends);
    }
}
