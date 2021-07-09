package com.example.chatting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatting.DAO.FriendDAO;
import com.example.chatting.Model.Friend;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.ImageConvert;
import com.example.chatting.Provider.SharedPreferenceProvider;
import com.example.chatting.R;

import java.util.List;

public class CommunityRecyclerAdapter extends RecyclerView.Adapter<CommunityRecyclerAdapter.ViewHolder>{

    User user;
    List<User> users;
    List<Friend> friends;
    public CommunityRecyclerAdapter(List<User> users, List<Friend> friends, User user) {
        this.users = users;
        this.friends = friends;
        this.user = user;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_avatar;
        TextView tv_name;
        ImageButton btn_add, btn_accept, btn_cancel;
        public ViewHolder(View itemView) {
            super(itemView);
            getView(itemView);
        }

        public void getView(View view){
            img_avatar = view.findViewById(R.id.img_avatar);
            tv_name = view.findViewById(R.id.tv_name);
            btn_add = view.findViewById(R.id.btn_add);
            btn_accept = view.findViewById(R.id.btn_accept);
            btn_cancel = view.findViewById(R.id.btn_cancel);
        }
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    public void setFriends(List<Friend> friends){this.friends = friends;}

    @Override
    public CommunityRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cartView = inflater.inflate(R.layout.community_item, parent, false);
        CommunityRecyclerAdapter.ViewHolder viewHolder = new CommunityRecyclerAdapter.ViewHolder(cartView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommunityRecyclerAdapter.ViewHolder holder, int position) {
        User mUser = users.get(position);
        Friend friend = friends.get(position);
        holder.tv_name.setText(mUser.getName());
        ImageConvert.setUrlToImageView(holder.img_avatar, mUser.getAvatar());
        if (friend == null){
            holder.btn_add.setVisibility(View.VISIBLE);
            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.GONE);
        }else {
            if (friend.isMyself() && user.getId().equals(friend.getUserId())){
                holder.btn_add.setVisibility(View.GONE);
                holder.btn_accept.setVisibility(View.GONE);
                holder.btn_cancel.setVisibility(View.VISIBLE);
            }else{
                holder.btn_add.setVisibility(View.GONE);
                holder.btn_accept.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.GONE);
            }
        }
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Friend addFriend = new Friend( user.getId(), mUser.getId());
                FriendDAO.getInstance().add(addFriend);
            }
        });

        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friend.setState(true);
                FriendDAO.getInstance().update(friend);
            }
        });

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendDAO.getInstance().remove(friend);
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
