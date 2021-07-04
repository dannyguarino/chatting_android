package com.example.chatting.DAO;

import com.example.chatting.Model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserDAO {

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

    public UserDAO(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    public Task<Void> add(User user){
//        return databaseReference.push().setValue(user);
        return databaseReference.child(user.getEmail()).setValue(user);
    }

    public Query get(String email){
        if (email == null){
           return databaseReference.orderByKey().limitToFirst(10);
        }
        return databaseReference.orderByKey().startAfter(email).limitToFirst(10);
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
        hashMap.put("n", user.getName());
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


}
