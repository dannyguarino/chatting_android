package com.example.chatting.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatting.Adapter.OnlineRecyclerAdapter;
import com.example.chatting.DAO.UserDAO;
import com.example.chatting.Model.ItemMain;
import com.example.chatting.Model.User;
import com.example.chatting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ONLINE = 1;
    private static final int TYPE_CHAT = 2;

    private List<ItemMain> items;
    // Constructor of the class
    public MainRecyclerAdapter(List<ItemMain> items) {
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

    public void loadDataOnline(ViewHolderOnline holder){
        UserDAO.getInstance().gets().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<User> usersOnline= new ArrayList<User>();
                for (DataSnapshot data: snapshot.getChildren()){
                    User user = data.getValue(User.class);
                    if (user.isState()) {
                        usersOnline.add(user);
                    }
                }
                updateDataOnline(holder, usersOnline);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void updateDataOnline(ViewHolderOnline holder, List<User> users) {
        holder.onlines.clear();
        holder.onlines.addAll(users);
        holder.onlineRecyclerAdapter.notifyDataSetChanged();
    }

    private void initLayoutChat(ViewHolderChat holder, int pos) {
        User user = items.get(pos).getChat();
        holder.tv_name.setText(user.getName());
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

        TextView tv_name;
        public ViewHolderChat(View itemView) {
            super(itemView);

            getView(itemView);
        }

        public void getView(View view){
            tv_name = view.findViewById(R.id.tv_name);
        }
    }

}
