package com.vik.learningchatapplication.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.vik.learningchatapplication.chat.ChatListModel;
import com.vik.learningchatapplication.chat.MessageModel;

@Database(entities = {MessageModel.class, ChatListModel.class}, version = 1, exportSchema = false)
public abstract class ChatDatabase extends RoomDatabase {
    public abstract ChatListDaoAccess daoAccess();
    public abstract MessageDaoAccess messageDaoAccess();
}
