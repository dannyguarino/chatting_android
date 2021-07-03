package com.example.chatting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatting.R;

import java.util.List;

public class OnlineRecyclerAdapter extends RecyclerView.Adapter<OnlineRecyclerAdapter.ViewHolder>{

    List<String> onlines;
    public OnlineRecyclerAdapter(List<String> onlines) {
        this.onlines = onlines;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

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
