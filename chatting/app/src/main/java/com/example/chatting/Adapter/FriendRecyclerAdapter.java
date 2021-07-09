package com.example.chatting.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatting.Activity.MessageActivity;
import com.example.chatting.DAO.FriendDAO;
import com.example.chatting.Model.Friend;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.ImageConvert;
import com.example.chatting.Provider.SharedPreferenceProvider;
import com.example.chatting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FriendRecyclerAdapter  extends RecyclerView.Adapter<FriendRecyclerAdapter.ViewHolder>{

    List<User> users;
    User user;
    public FriendRecyclerAdapter(List<User> users, User user) {
        this.users = users;
        this.user = user;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_friend;
        ImageView img_avatar;
        TextView tv_name;
        ImageButton btn_cancel;

        public ViewHolder(View itemView) {
            super(itemView);
            getView(itemView);
        }

        public void getView(View view){
            layout_friend = view.findViewById(R.id.layout_friend);
            img_avatar = view.findViewById(R.id.img_avatar);
            tv_name = view.findViewById(R.id.tv_name);
            btn_cancel = view.findViewById(R.id.btn_cancel);
        }
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public FriendRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cartView = inflater.inflate(R.layout.friend_item, parent, false);
        FriendRecyclerAdapter.ViewHolder viewHolder = new FriendRecyclerAdapter.ViewHolder(cartView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendRecyclerAdapter.ViewHolder holder, int position) {
        User mUser = users.get(position);
        holder.tv_name.setText(mUser.getName());
        ImageConvert.setUrlToImageView(holder.img_avatar, mUser.getAvatar());

        holder.layout_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), MessageActivity.class);
                intent.putExtra("friend", mUser);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendDAO.getInstance().gets(user).addValueEventListener(new ValueEventListener() {
                    boolean isDeleted = false;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data: snapshot.getChildren()){
                            Friend friend = data.getValue(Friend.class);
                            if (friend.getFriendId().equals(mUser.getId())) {
                                FriendDAO.getInstance().remove(friend);
                                isDeleted = true;
                                removeItem(position);
                                break;
                            }
                        }
                        if (!isDeleted){
                            FriendDAO.getInstance().getsFriendId(user).addValueEventListener(new ValueEventListener() {
                                boolean isDeleted = false;
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot data: snapshot.getChildren()){
                                        Friend friend = data.getValue(Friend.class);
                                        if (friend.getUserId().equals(mUser.getId())) {
                                            FriendDAO.getInstance().remove(friend);
                                            removeItem(position);
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : -1;
    }

    private void removeItem(int position) {
        users.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, users.size());
    }
}
