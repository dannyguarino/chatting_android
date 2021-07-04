package com.example.chatting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatting.DAO.UserDAO;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.ImageConvert;
import com.example.chatting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OnlineRecyclerAdapter extends RecyclerView.Adapter<OnlineRecyclerAdapter.ViewHolder>{

    List<User> onlines;
    public OnlineRecyclerAdapter(List<User> onlines) {
        this.onlines = onlines;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_avatar;
        TextView tv_name;
        public ViewHolder(View itemView) {
            super(itemView);
            getView(itemView);
        }

        public void getView(View view){
            img_avatar = view.findViewById(R.id.img_avatar);
            tv_name = view.findViewById(R.id.tv_name);
        }
    }

    @Override
    public OnlineRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cartView = inflater.inflate(R.layout.online_item, parent, false);
        OnlineRecyclerAdapter.ViewHolder viewHolder = new OnlineRecyclerAdapter.ViewHolder(cartView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OnlineRecyclerAdapter.ViewHolder holder, int position) {
        User user = onlines.get(position);
        holder.tv_name.setText(user.getName());
        ImageConvert.setUrlToImageView(holder.img_avatar, user.getAvatar());
    }

    @Override
    public int getItemCount() {
        return onlines != null ? onlines.size() : -1;
    }

    private void removeItem(int position) {
        onlines.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, onlines.size());
    }


}
