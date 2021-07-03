package com.example.chatting.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatting.Model.ItemMain;
import com.example.chatting.R;

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
        holder.onlines = item.getOnlines();
        holder.setView(holder.itemView);
    }

    private void initLayoutChat(ViewHolderChat holder, int pos) {
//        holder.tvLeft.setText(itemList.get(pos).getName());
//        holder.tvRight.setText(itemList.get(pos).getName());
    }


    // Static inner class to initialize the views of rows
    static class ViewHolderOnline extends RecyclerView.ViewHolder {

        OnlineRecyclerAdapter onlineRecyclerAdapter;

        List<String> onlines;

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

        public ViewHolderChat(View itemView) {
            super(itemView);

        }
    }

}
