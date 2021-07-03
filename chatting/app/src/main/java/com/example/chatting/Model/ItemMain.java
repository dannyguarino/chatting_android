package com.example.chatting.Model;

import java.util.List;

public class ItemMain {
    public enum ItemType {
        ONLINE_ITEM, CHAT_ITEM;
    }

    private List<String> onlines;
    private String chat;
    private ItemType type;

    public ItemMain(String chat, ItemType type) {
        this.chat = chat;
        this.type = type;
    }

    public ItemMain(List<String> onlines, ItemType type) {
        this.onlines = onlines;
        this.type = type;
    }



    public ItemMain(List<String> onlines, String chat, ItemType type) {
        this.onlines = onlines;
        this.chat = chat;
        this.type = type;
    }

    public List<String> getOnlines() {
        return onlines;
    }

    public void setOnlines(List<String> onlines) {
        this.onlines = onlines;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }
}
