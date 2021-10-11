package com.vik.learningchatapplication.db;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.vik.learningchatapplication.chat.ChatListModel;
import com.vik.learningchatapplication.chat.MessageModel;

import java.util.List;

public class ChatRepository {
    private final static String DB_NAME = "chat_db";

    private ChatDatabase database = null;

    private static ChatRepository instance = null;


    private ChatRepository(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, ChatDatabase.class, DB_NAME).build();
        }
    }

    public static ChatRepository getInstance(Context context) {
        if(instance == null) {
            instance = new ChatRepository(context);
        }
        return instance;
    }

    public void insertMessage(MessageModel messageModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.messageDaoAccess().insertTask(messageModel);
            }
        }).start();
    }

    public void insertChatItem(ChatListModel messageModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.daoAccess().insertTask(messageModel);
            }
        }).start();
    }

    public LiveData<List<ChatListModel>> getTasks() {
        return database.daoAccess().fetchAllChatListModel();
    }

    public LiveData<List<MessageModel>> getMessages(String currentUserId, String chatUserId) {
        return database.messageDaoAccess().fetchAllChatListModel(currentUserId, chatUserId);
    }



}
