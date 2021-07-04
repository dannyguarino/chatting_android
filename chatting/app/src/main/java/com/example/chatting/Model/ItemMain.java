package com.example.chatting.Model;

import java.util.List;

public class ItemMain {
    public enum ItemType {
        ONLINE_ITEM, CHAT_ITEM;
    }

    private List<User> onlines;
    private User chat;
    private ItemType type;

    public ItemMain(User chat, ItemType type) {
        this.chat = chat;
        this.type = type;
    }

    public ItemMain(List<User> onlines, ItemType type) {
        this.onlines = onlines;
        this.type = type;
    }

    public ItemMain(List<User> onlines, User chat, ItemType type) {
        this.onlines = onlines;
        this.chat = chat;
        this.type = type;
    }

    public List<User> getOnlines() {
        return onlines;
    }

    public void setOnlines(List<User> onlines) {
        this.onlines = onlines;
    }

    public User getChat() {
        return chat;
    }

    public void setChat(User chat) {
        this.chat = chat;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }
}
