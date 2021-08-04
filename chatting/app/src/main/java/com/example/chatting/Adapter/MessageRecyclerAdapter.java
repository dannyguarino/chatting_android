package com.example.chatting.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatting.Activity.MessageActivity;
import com.example.chatting.DAO.MessageDAO;
import com.example.chatting.DAO.UserDAO;
import com.example.chatting.Model.Message;
import com.example.chatting.Model.User;
import com.example.chatting.Provider.DateProvider;
import com.example.chatting.Provider.ImageConvert;
import com.example.chatting.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder>{

    List<Message> messages;
    BottomSheetDialog bottomSheetDialog, bottomSheetDialogRemove;
    View bottomSheetView, bottomSheetViewRemove;
    public MessageRecyclerAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_message, layout_message_friend, layout_time,
                layout_message_main;
        CardView card_message, card_message_friend;
        ImageView img_message, img_message_friend, img_avatar, img_status;
        TextView tv_message, tv_message_friend, tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            getView(itemView);
        }

        public void getView(View view){
            layout_message_main = view.findViewById(R.id.layout_message_main);
            layout_message = view.findViewById(R.id.layout_message);
            layout_message_friend = view.findViewById(R.id.layout_message_friend);
            layout_time = view.findViewById(R.id.layout_time);
            card_message = view.findViewById(R.id.card_message);
            card_message_friend = view.findViewById(R.id.card_message_friend);
            img_message = view.findViewById(R.id.img_message);
            img_message_friend = view.findViewById(R.id.img_message_friend);
            img_avatar = view.findViewById(R.id.img_avatar);
            img_status = view.findViewById(R.id.img_status);
            tv_message = view.findViewById(R.id.tv_message);
            tv_message_friend = view.findViewById(R.id.tv_message_friend);
            tv_time = view.findViewById(R.id.tv_time);
        }
    }

    public void setMessages(List<Message> messages){
        this.messages = messages;
    }

    @Override
    public MessageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cartView = inflater.inflate(R.layout.message_item, parent, false);
        MessageRecyclerAdapter.ViewHolder viewHolder = new MessageRecyclerAdapter.ViewHolder(cartView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageRecyclerAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (message.isMyself()){
            holder.layout_message_friend.setVisibility(View.GONE);
            holder.layout_message.setVisibility(View.VISIBLE);
            holder.layout_message_main.setGravity(Gravity.RIGHT);
            if (message.getStatus() == 0){
                holder.img_status.setVisibility(View.VISIBLE);
                holder.img_status.setImageResource(R.drawable.ic_baseline_unsent);
            }else if (message.getStatus() == 1){
                holder.img_status.setVisibility(View.VISIBLE);
                holder.img_status.setImageResource(R.drawable.ic_baseline_sent);
            }else if (message.getStatus() == 2){
                holder.img_status.setVisibility(View.VISIBLE);
                holder.img_status.setImageResource(R.drawable.ic_baseline_received);
            }else{
                if (message.getId().equals(messages.get(messages.size() - 1).getId())){
                    holder.img_status.setVisibility(View.VISIBLE);
                    holder.img_status.setImageResource(R.drawable.ic_baseline_seen);
                }else{
                    holder.img_status.setVisibility(View.INVISIBLE);
                }
            }
            holder.img_avatar.setVisibility(View.GONE);
        }else {
            holder.layout_message_friend.setVisibility(View.VISIBLE);
            holder.layout_message.setVisibility(View.GONE);
            holder.layout_message_main.setGravity(Gravity.LEFT);
            holder.img_status.setVisibility(View.GONE);
//            UserDAO.getInstance().get(message.getFriendId()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot data: snapshot.getChildren()){
//                        User friend = data.getValue(User.class);
//                        ImageConvert.setUrlToImageView(holder.img_avatar, friend.getAvatar());
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }

        if (message.getType().equals("message")){
            holder.img_message.setVisibility(View.GONE);
            holder.img_message_friend.setVisibility(View.GONE);
            holder.tv_message.setVisibility(View.VISIBLE);
            holder.tv_message_friend.setVisibility(View.VISIBLE);
            if (message.isMyself()){
                holder.tv_message.setText(message.getContext());
            }else {
                holder.tv_message_friend.setText(message.getContext());
            }
        }else if(message.getType().equals("image")){
            holder.img_message.setVisibility(View.VISIBLE);
            holder.img_message_friend.setVisibility(View.VISIBLE);
            holder.tv_message.setVisibility(View.GONE);
            holder.tv_message_friend.setVisibility(View.GONE);
            if (message.isMyself()){
                holder.img_message.setImageBitmap(ImageConvert.StringToBitMap(message.getContext()));
            }else {
                holder.img_message_friend.setImageBitmap(ImageConvert.StringToBitMap(message.getContext()));
            }
        }

        Date datetime = new Date(message.getTime());
        String time = DateProvider.datetimeFormat.format(datetime);
        String now = DateProvider.getDateTimeNow();

        if (time.split(" ")[0].equals(now.split(" ")[0])){
            holder.tv_time.setText(time.split(" ")[1]);
        }else {
            holder.tv_time.setText(DateProvider.convertDateTimeSqliteToPerson(time));
        }

        View.OnClickListener onClickListenerTime = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isVisible = holder.layout_time.getVisibility() == View.VISIBLE;
                if (isVisible){
//                    holder.layout_time.setVisibility(View.GONE);
                    holder.layout_time.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    holder.layout_time.setVisibility(View.GONE);
                                }
                            });
                }else{
//                    holder.layout_time.setVisibility(View.VISIBLE);
                    holder.layout_time.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    holder.layout_time.setVisibility(View.VISIBLE);
                                }
                            });
                }
            }
        };

        holder.layout_message.setOnClickListener(onClickListenerTime);
        holder.layout_message_friend.setOnClickListener(onClickListenerTime);

        final CountDownTimer[] countDownTimer = {null};
        View.OnTouchListener onClickListenerShow = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    countDownTimer[0] = new CountDownTimer(500, 500){
                        public void onTick(long millisUntilFinished){

                        }
                        public  void onFinish(){
                            showDialog(holder.itemView, message);
                        }
                    }.start();
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    if (countDownTimer[0] != null) {
                        countDownTimer[0].cancel();
                    }
                }
                return false;
            }
        };

        holder.layout_message.setOnTouchListener(onClickListenerShow);
        holder.layout_message_friend.setOnTouchListener(onClickListenerShow);

    }

    public void showDialog(View view, Message message){
        bottomSheetDialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetView = LayoutInflater.from(view.getContext())
                .inflate(R.layout.bottom_sheet_funtion_message, view.findViewById(R.id.btnSheetContainer));
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        bottomSheetView.findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRemove(view, message);
                bottomSheetDialog.hide();
            }
        });
    }

    public void showDialogRemove(View view, Message message){
        bottomSheetDialogRemove = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetViewRemove = LayoutInflater.from(view.getContext())
                .inflate(R.layout.bottom_sheet_remove, view.findViewById(R.id.btnSheetContainer));
        bottomSheetDialogRemove.setContentView(bottomSheetViewRemove);
        bottomSheetDialogRemove.show();

        if (!message.isMyself()){
            bottomSheetViewRemove.findViewById(R.id.btn_unsend).setVisibility(View.GONE);
        }

        bottomSheetViewRemove.findViewById(R.id.btn_unsend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MessageDAO.getInstance().remove(message);
                MessageDAO.getInstance().unsend(message);
                bottomSheetDialogRemove.hide();
            }
        });

        bottomSheetViewRemove.findViewById(R.id.btn_remove_for_you).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDAO.getInstance().remove(message);
                removeItem(message);
                bottomSheetDialogRemove.hide();
            }
        });
    }


    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : -1;
    }

    private void removeItem(int position) {
        messages.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, messages.size());
    }

    private void removeItem(Message message) {
        int position = messages.indexOf(message);
        MessageActivity.rc_message.getLayoutManager().scrollToPosition(position - 1);
        messages.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, messages.size());
    }
}
