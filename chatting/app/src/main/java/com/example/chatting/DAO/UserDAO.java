package com.example.chatting.DAO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatting.Activity.MainActivity;
import com.example.chatting.Model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserDAO {

    private static List<User> users;

    private static final Object lock = new Object();
    private static UserDAO instance;
    public static UserDAO getInstance(){
        synchronized (lock){
            if (instance == null){
                instance = new UserDAO();
            }
            return instance;
        }
    }

    private static DatabaseReference databaseReference;
    private static FirebaseDatabase db;

    public UserDAO(){
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
        users = new ArrayList<User>();
    }

    public Task<Void> add(User user){
//        return databaseReference.push().setValue(user);
        String userId = databaseReference.push().getKey();
        user.setId(userId);
        return databaseReference.child(user.getId()).setValue(user);
    }

    public Query get(String name, int limit){
        if (name == null){
           return databaseReference.orderByChild("name").limitToFirst(limit);
        }
        return databaseReference.orderByChild(name).startAfter(name).limitToFirst(limit);
    }

    public Query get(String id){
        return databaseReference.orderByChild("id").equalTo(id).limitToFirst(1);
    }

    public User getByEmail(String email){
        getUserData(new FirebaseCallBack() {
            @Override
            public void onCallBack(List<User> mUsers) {
                users = mUsers;
                System.out.println("User " + users.get(0).getEmail() );
            }
        }, email);
//        databaseReference.orderByChild("email").equalTo(email).addValueEventListener(valueEventListener);
        if (users.size() > 0){
            return users.get(0);
        }
        return null;
    }

    public Query gets(){
        return databaseReference.orderByKey();
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap){
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> update(String key, User user){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", user.getId());
        hashMap.put("avatar", user.getAvatar());
        hashMap.put("name", user.getName());
        hashMap.put("email", user.getEmail());
        hashMap.put("password", user.getPassword());
        hashMap.put("timeOff", user.getTimeOff());
        hashMap.put("createdDate", user.getCreatedDate());
        hashMap.put("state", user.isState());
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot data: snapshot.getChildren()){
                User user = data.getValue(User.class);
                users.add(user);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void getUserData(FirebaseCallBack firebaseCallBack, String email){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> users = new ArrayList<User>();
                for (DataSnapshot data: snapshot.getChildren()){
                    User user = data.getValue(User.class);
                    users.add(user);
                }
                firebaseCallBack.onCallBack(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.orderByChild("email").equalTo(email).addValueEventListener(valueEventListener);
    }

    public interface FirebaseCallBack{
        void onCallBack(List<User> users);
    }

}
