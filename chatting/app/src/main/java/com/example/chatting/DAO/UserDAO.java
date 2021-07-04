package com.example.chatting.DAO;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chatting.Model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren())
                {
                    //lấy key của dữ liệu
                    String key=data.getKey();
                    //lấy giá trị của key (nội dung)
                    String value=data.getValue().toString();
                    System.out.println(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public Task<Void> add(User user){
        System.out.println("''''''''''''''''''''''" + databaseReference);
        databaseReference.setValue("heelle");
        return databaseReference.push().setValue(user);
    }

}
