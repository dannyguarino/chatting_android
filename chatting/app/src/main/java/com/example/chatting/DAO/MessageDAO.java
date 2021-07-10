package com.example.chatting.DAO;

import androidx.annotation.NonNull;

import com.example.chatting.Model.Friend;
import com.example.chatting.Model.Message;
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

public class MessageDAO {

    private static List<Message> messages;

    private static final Object lock = new Object();
    private static MessageDAO instance;
    public static MessageDAO getInstance(){
        synchronized (lock){
            if (instance == null){
                instance = new MessageDAO();
            }
            return instance;
        }
    }

    public DatabaseReference databaseReference;
    public FirebaseDatabase db;

    public MessageDAO(){
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Message.class.getSimpleName());
        messages = new ArrayList<Message>();
    }

    public Task<Void> add(Message message){
        String messageId = databaseReference.push().getKey();
        message.setId(messageId);
        return databaseReference.child(messageId).setValue(message);
    }

    public Query gets(User user, User friend){
        return databaseReference.orderByChild("userId").equalTo(user.getId());
    }

    public Query gets(User user){
        return databaseReference.orderByChild("userId").equalTo(user.getId());
    }

    public Query getLastMessage(User user){
        return databaseReference.orderByChild("userId").equalTo(user.getId()).limitToLast(1);
    }

    public Task<Void> update(Message message){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", message.getId());
        hashMap.put("userId", message.getUserId());
        hashMap.put("friendId", message.getFriendId());
        hashMap.put("context", message.getContext());
        hashMap.put("time", message.getTime());
        hashMap.put("type", message.getType());
        hashMap.put("myself", message.isMyself());
        hashMap.put("state", message.isState());
        return databaseReference.child(message.getId()).updateChildren(hashMap);
    }

    public Task<Void> remove(Message message){

        return databaseReference.child(message.getId()).removeValue();
    }

    public void unsend(Message message){
        remove(message);
        databaseReference.orderByChild("friendId").equalTo(message.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data: snapshot.getChildren()){
                            Message message02 = data.getValue(Message.class);
                            if (message02.getUserId().equals(message.getFriendId())
                            && message02.getTime() == message.getTime() && !message02.isMyself()){
                                remove(message02);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
