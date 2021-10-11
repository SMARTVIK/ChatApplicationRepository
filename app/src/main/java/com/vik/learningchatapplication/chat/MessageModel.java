package com.vik.learningchatapplication.chat;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MessageModel implements Parcelable {
    private  String message;
    private  String messageFrom;

    @PrimaryKey
    @NonNull
    private  String messageId;
    private  long messageTime;
    private  String messageType;

    public MessageModel() {
    }

    public MessageModel(String message, String messageFrom, String messageId, long messageTime, String messageType) {
        this.message = message;
        this.messageFrom = messageFrom;
        this.messageId = messageId;
        this.messageTime = messageTime;
        this.messageType = messageType;
    }

    protected MessageModel(Parcel in) {
        message = in.readString();
        messageFrom = in.readString();
        messageId = in.readString();
        messageTime = in.readLong();
        messageType = in.readString();
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(messageFrom);
        parcel.writeString(messageId);
        parcel.writeLong(messageTime);
        parcel.writeString(messageType);
    }
}
