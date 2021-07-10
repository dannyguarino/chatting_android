package com.example.chatting.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatting.Adapter.MessageRecyclerAdapter;
import com.example.chatting.Application.App;
import com.example.chatting.DAO.MessageDAO;
import com.example.chatting.DAO.UserDAO;
import com.example.chatting.Model.Message;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.ImageConvert;
import com.example.chatting.Provider.SharedPreferenceProvider;
import com.example.chatting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{

    private int SELECT_PICTURES = 123;

    List<Message> messages;
    public static User user, friend;
    Intent intent;

    MessageRecyclerAdapter messageRecyclerAdapter;

    ImageButton btn_back, btn_send, btn_image;
    TextView tv_name;
    ImageView img_avatar;
    EditText txt_message;
    public static RecyclerView rc_message;
    SwipeRefreshLayout swipe_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getModel();
        getView();
        setView();
        setOnClick();

//        notificationManagerCompat = NotificationManagerCompat.from(this);
//        getLastMessage();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        friend = null;
    }

    public void getModel(){
        messages = new ArrayList<>();
        user = (User) SharedPreferenceProvider.getInstance(this).get("user");

        intent = getIntent();
        friend = (User)intent.getSerializableExtra("friend");
    }

    public void getView(){
        btn_back = findViewById(R.id.btn_back);
        btn_send = findViewById(R.id.btn_send);
        btn_image = findViewById(R.id.btn_image);
        tv_name = findViewById(R.id.tv_name);
        img_avatar = findViewById(R.id.img_avatar);
        txt_message = findViewById(R.id.txt_message);
        rc_message = findViewById(R.id.rc_message);
//        swipe_message = findViewById(R.id.swipe_message);
    }

    public void setView(){
        ImageConvert.setUrlToImageView(img_avatar, friend.getAvatar());
        tv_name.setText(friend.getName());
        messageRecyclerAdapter = new MessageRecyclerAdapter(messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        linearLayoutManager.setStackFromEnd(true);
        rc_message.setLayoutManager(linearLayoutManager);
        rc_message.setItemAnimator(new DefaultItemAnimator());
        rc_message.setAdapter(messageRecyclerAdapter);
        loadData();
    }

    public void setOnClick(){
        btn_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_image.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_send:
                send();
                break;
            case R.id.btn_image:
                selectImage();
                break;
        }
    }

    void loadData(){
        MessageDAO.getInstance().gets(user, friend).addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            messages = new ArrayList<>();
            for (DataSnapshot data: snapshot.getChildren()){
                Message message = data.getValue(Message.class);
                if (message.getFriendId().equals(friend.getId())){
                    messages.add(message);
                }
                messageRecyclerAdapter.setMessages(messages);
                messageRecyclerAdapter.notifyDataSetChanged();
                rc_message.getLayoutManager().scrollToPosition(messages.size() - 1);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void send(){
        String txtMessage = txt_message.getText().toString();
        if (!txtMessage.equals("")){
            addMessage(txtMessage, "message");
        }
    }

    public void selectImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
//**The following line is the important one!
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        Intent chooserIntent = Intent.createChooser(intent, "Chọn ảnh");
        startActivityForResult(chooserIntent, SELECT_PICTURES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PICTURES) {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    System.out.println("==== " + count);
                    for(int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            String content = ImageConvert.BitMapToString(bitmap);
                            addMessage(content, "image");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                } else if(data.getData() != null) {
                    Uri imageUri  = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        String content = ImageConvert.BitMapToString(bitmap);
                        addMessage(content, "image");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void addMessage(String context, String type){
        Message message = new Message(user.getId(), friend.getId(), context, type, true, true);
        Message messageFriend = new Message(friend.getId(), user.getId(), context, type, false, false);
        MessageDAO.getInstance().add(message);
        MessageDAO.getInstance().add(messageFriend);
        messages.add(message);
        messageRecyclerAdapter.notifyDataSetChanged();
        txt_message.setText("");
        rc_message.getLayoutManager().scrollToPosition(messages.size() - 1);
    }
}