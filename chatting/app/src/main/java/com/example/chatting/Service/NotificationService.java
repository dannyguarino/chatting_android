package com.example.chatting.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.chatting.Activity.MessageActivity;
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

import java.util.Date;

public class NotificationService extends Service {

    NotificationManagerCompat notificationManagerCompat;
    public static User user;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        user = (User) SharedPreferenceProvider.getInstance(getApplicationContext()).get("user");
        notificationManagerCompat = NotificationManagerCompat.from(this);
        getLastMessage();
        return START_STICKY;
    }

    void getLastMessage() {
        MessageDAO.getInstance().getLastMessage(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    if (!message.isMyself() && !message.isState()) {
                        UserDAO.getInstance().get(message.getFriendId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    User user = data.getValue(User.class);
                                    if (MessageActivity.friend == null || !MessageActivity.friend.getId().equals(user.getId()))
                                        sendNotification(user, message);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void sendNotification(User user, Message message) {
        String context = message.getContext();
        if (message.getType().equals("image"))
            context = "Tin nhắn hình ảnh";
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("friend", user);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                .setLabel("Nội dung ....")
                .build();

        Bitmap avatar = ImageConvert.getBitmapFromURL(this, user.getAvatar());

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), App.CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo_no_background)
                .setLargeIcon(avatar)
                .setContentTitle(user.getName())
                .setContentText(context)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManagerCompat.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notification);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify("tag", (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notification);
//        startForeground((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notification);
        message.setState(true);
        MessageDAO.getInstance().update(message);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        user = (User) SharedPreferenceProvider.getInstance(getApplicationContext()).get("user");
        notificationManagerCompat = NotificationManagerCompat.from(this);
        getLastMessage();
    }
}
