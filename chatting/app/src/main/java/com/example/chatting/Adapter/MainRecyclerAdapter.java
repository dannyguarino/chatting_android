package com.example.chatting.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatting.Activity.MessageActivity;
import com.example.chatting.DAO.FriendDAO;
import com.example.chatting.DAO.UserDAO;
import com.example.chatting.Model.Friend;
import com.example.chatting.Model.ItemMain;
import com.example.chatting.Model.Message;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.DateProvider;
import com.example.chatting.Provider.ImageConvert;
import com.example.chatting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ONLINE = 1;
    private static final int TYPE_CHAT = 2;

    private List<ItemMain> items;
    User user;
    // Constructor of the class
    public MainRecyclerAdapter(List<ItemMain> items, User user) {
        this.items = items;
        this.user = user;
    }

    public void setItems(List<ItemMain> items){
        this.items = items;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    // determine which layout to use for the row
    @Override
    public int getItemViewType(int position) {
        ItemMain item = items.get(position);
        if (item.getType() == ItemMain.ItemType.ONLINE_ITEM) {
            return TYPE_ONLINE;
        } else if (item.getType() == ItemMain.ItemType.CHAT_ITEM) {
            return TYPE_CHAT;
        } else {
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONLINE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_online_item, parent, false);
            return new ViewHolderOnline(view);
        } else if (viewType == TYPE_CHAT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
            return new ViewHolderChat(view);
        } else {
            throw new RuntimeException("The type has to be ONE or TWO");
        }
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int listPosition) {
        switch (holder.getItemViewType()) {
            case TYPE_ONLINE:
                initLayoutOnline((ViewHolderOnline)holder, listPosition);
                break;
            case TYPE_CHAT:
                initLayoutChat((ViewHolderChat) holder, listPosition);
                break;
            default:
                break;
        }
    }

    private void initLayoutOnline(ViewHolderOnline holder, int pos) {
        ItemMain item = items.get(pos);
        loadDataOnline(holder);
        holder.onlines = item.getOnlines();
        holder.setView(holder.itemView);
    }

    public void loadDataOnline(ViewHolderOnline holder) {
        List<User> usersOnline = new ArrayList<User>();
        FriendDAO.getInstance().getFriendData(new FriendDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(List<Friend> friends) {
                for (Friend friend : friends) {
                    if (friend.isState()) {
                        UserDAO.getInstance().get(friend.getFriendId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    User mUser = data.getValue(User.class);
                                    if (mUser.isState() && !user.getId().equals(mUser.getId())) {
                                        usersOnline.add(mUser);
                                    }
                                }
                                UserDAO.getInstance().get(friend.getUserId()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        List<User> usersOnline = new ArrayList<User>();
                                        for (DataSnapshot data : snapshot.getChildren()) {
                                            User mUser = data.getValue(User.class);
                                            if (mUser.isState() && !user.getId().equals(mUser.getId())) {
                                                usersOnline.add(mUser);
                                            }
                                        }
                                        updateDataOnline(holder, usersOnline);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        }, user);
    }

    public void updateDataOnline(ViewHolderOnline holder, List<User> users) {
        holder.onlines.clear();
        holder.onlines.addAll(users);
        holder.onlineRecyclerAdapter.notifyDataSetChanged();
    }

    private void initLayoutChat(ViewHolderChat holder, int pos) {
        User friend = items.get(pos).getChat();
        Message message = items.get(pos).getMessage();
        holder.tv_name.setText(friend.getName());
        ImageConvert.setUrlToImageView(holder.img_avatar, friend.getAvatar());
        String context = message.getContext();
        if (message.getType().equals("image"))
            context = "Tin nhắn hình ảnh";
        if (message.isMyself())
            context = "Bạn: " + context;
        holder.tv_context.setText(context);
        Date datetime = new Date(message.getTime());
        String time = DateProvider.datetimeFormat.format(datetime);
        String now = DateProvider.getDateTimeNow();
        if (time.split(" ")[0].equals(now.split(" ")[0])){
            holder.tv_time.setText(time.split(" ")[1]);
        }else {
            holder.tv_time.setText(DateProvider.convertDateTimeSqliteToPerson(time));
        }

        holder.layout_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), MessageActivity.class);
                intent.putExtra("friend", friend);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    // Static inner class to initialize the views of rows
    static class ViewHolderOnline extends RecyclerView.ViewHolder {

        OnlineRecyclerAdapter onlineRecyclerAdapter;

        List<User> onlines;

        RecyclerView rc_online;
        public ViewHolderOnline(View itemView) {
            super(itemView);

            getView(itemView);
        }

        public void getView(View view){
            rc_online = view.findViewById(R.id.rc_online);
        }

        public void setView(View view){
            onlineRecyclerAdapter = new OnlineRecyclerAdapter(onlines);
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
            rc_online.setLayoutManager(layoutManager);
            rc_online.setItemAnimator(new DefaultItemAnimator());
            rc_online.setAdapter(onlineRecyclerAdapter);
        }
    }

    static class ViewHolderChat extends RecyclerView.ViewHolder {
        LinearLayout layout_chat;
        ImageView img_avatar;
        TextView tv_name, tv_context, tv_time;
        public ViewHolderChat(View itemView) {
            super(itemView);

            getView(itemView);
        }

        public void getView(View view){
            layout_chat = view.findViewById(R.id.layout_chat);
            tv_name = view.findViewById(R.id.tv_name);
            img_avatar = view.findViewById(R.id.img_avatar);
            tv_context = view.findViewById(R.id.tv_context);
            tv_time = view.findViewById(R.id.tv_time);
        }
    }

}
