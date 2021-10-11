package com.vik.learningchatapplication.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vik.learningchatapplication.chat.MessageModel;

import java.util.List;

@Dao
public interface MessageDaoAccess {

    @Insert
    Long insertTask(MessageModel note);


    @Query("SELECT * FROM MessageModel where messageId =:currentUserId and messageFrom =:chatUserId")
    LiveData<List<MessageModel>> fetchAllChatListModel(String currentUserId, String chatUserId);


    @Query("SELECT * FROM MessageModel WHERE messageId =:taskId")
    LiveData<MessageModel> getMessage(int taskId);


    @Update
    void updateTask(MessageModel note);


    @Delete
    void deleteTask(MessageModel note);
}
