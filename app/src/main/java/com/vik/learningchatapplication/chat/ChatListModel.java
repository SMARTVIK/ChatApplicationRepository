package com.vik.learningchatapplication.chat;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatListModel implements Parcelable {

    @PrimaryKey
    @NonNull
    private  String userId;
    private  String userName;
    private  String photoName;
    private  String unreadCount;
    private  String lastMessage;
    private  String lastMessageTime;

    public ChatListModel(String userId, String userName, String photoName, String unreadCount, String lastMessage, String lastMessageTime) {
        this.userId = userId;
        this.userName = userName;
        this.photoName = photoName;
        this.unreadCount = unreadCount;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    protected ChatListModel(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        photoName = in.readString();
        unreadCount = in.readString();
        lastMessage = in.readString();
        lastMessageTime = in.readString();
    }

    public static final Creator<ChatListModel> CREATOR = new Creator<ChatListModel>() {
        @Override
        public ChatListModel createFromParcel(Parcel in) {
            return new ChatListModel(in);
        }

        @Override
        public ChatListModel[] newArray(int size) {
            return new ChatListModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(userName);
        parcel.writeString(photoName);
        parcel.writeString(unreadCount);
        parcel.writeString(lastMessage);
        parcel.writeString(lastMessageTime);
    }
}
