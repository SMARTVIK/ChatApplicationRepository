package com.vik.learningchatapplication.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vik.learningchatapplication.chat.ChatListModel;

import java.util.List;

@Dao
public interface ChatListDaoAccess {

    @Insert
    Long insertTask(ChatListModel note);


    @Query("SELECT * FROM ChatListModel")
    LiveData<List<ChatListModel>> fetchAllChatListModel();


    @Query("SELECT * FROM ChatListModel WHERE userId =:taskId")
    LiveData<ChatListModel> getChat(int taskId);


    @Update
    void updateTask(ChatListModel note);


    @Delete
    void deleteTask(ChatListModel note);
}
