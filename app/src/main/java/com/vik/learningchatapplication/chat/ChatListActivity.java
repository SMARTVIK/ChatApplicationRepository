package com.vik.learningchatapplication.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vik.learningchatapplication.R;
import com.vik.learningchatapplication.common.NodeNames;
import com.vik.learningchatapplication.common.User;
import com.vik.learningchatapplication.common.Util;
import com.vik.learningchatapplication.db.ChatRepository;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private static final String TAG = "ChatFragment";
    private RecyclerView rvChatList;
    private View progressBar;
    private TextView tvEmptyChatList;
    private  ChatListAdapter chatListAdapter;
    private List<ChatListModel> chatListModelList;
    private ChatRepository chatRepository;

    private DatabaseReference databaseReferenceChats, databaseReferenceUsers;
    private FirebaseUser currentUser;

    private ChildEventListener childEventListener;


    private  List<String> userIds;
    private Query query;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        chatRepository = ChatRepository.getInstance(this);
        User user = new User("Vivek");
        Util.saveDataInStorage(this, user);
        findViewById(R.id.tvEmptyChatList).setOnClickListener(v -> {
            User user1 = Util.getDataFromStorage(this);
            Log.d(TAG, "onCreate: "+user1.name);
        });
        initViews();
        initOtherViewsAndListeners();
    }

    private void initOtherViewsAndListeners() {
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child(NodeNames.USERS);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "initOtherViewsAndListeners: "+currentUser.getDisplayName());
        databaseReferenceChats = FirebaseDatabase.getInstance().getReference().child(NodeNames.CHATS).child(currentUser.getUid());
        query = databaseReferenceChats.orderByChild(NodeNames.TIME_STAMP);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: ");
                showProgressBar();
                updateList(dataSnapshot, true, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged: ");
                showProgressBar();
                updateList(dataSnapshot, false, dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: ");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildMoved: ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
                hideProgressBar();
            }
        };
        query.addChildEventListener(childEventListener);
        hideProgressBar();
        tvEmptyChatList.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        rvChatList = findViewById(R.id.rvChats);
        tvEmptyChatList = findViewById(R.id.tvEmptyChatList);
        userIds = new ArrayList<>();
        chatListModelList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(this, chatListModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvChatList.setLayoutManager(linearLayoutManager);
        rvChatList.setAdapter(chatListAdapter);
        progressBar = findViewById(R.id.progressBar);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private  void updateList(DataSnapshot dataSnapshot, final boolean isNew, final String userId)
    {
        hideProgressBar();
        tvEmptyChatList.setVisibility(View.GONE);

        final String  lastMessage, lastMessageTime, unreadCount;

        if(dataSnapshot.child(NodeNames.LAST_MESSAGE).getValue()!=null)
            lastMessage = dataSnapshot.child(NodeNames.LAST_MESSAGE).getValue().toString();
        else
            lastMessage = "";

        if(dataSnapshot.child(NodeNames.LAST_MESSAGE_TIME).getValue()!=null)
            lastMessageTime = dataSnapshot.child(NodeNames.LAST_MESSAGE_TIME).getValue().toString();
        else
            lastMessageTime="";

        unreadCount=dataSnapshot.child(NodeNames.UNREAD_COUNT).getValue()==null?
                "0":dataSnapshot.child(NodeNames.UNREAD_COUNT).getValue().toString();

        databaseReferenceUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fullName = dataSnapshot.child(NodeNames.NAME).getValue()!=null?
                        dataSnapshot.child(NodeNames.NAME).getValue().toString():"";

                /*String photoName = dataSnapshot.child(NodeNames.PHOTO).getValue()!=null?
                        dataSnapshot.child(NodeNames.PHOTO).getValue().toString():"";*/
                String photoName  = userId +".jpg";

                ChatListModel chatListModel = new ChatListModel(userId, fullName, photoName,unreadCount,lastMessage,lastMessageTime);

                if (isNew) {
                    chatListModelList.add(chatListModel);
                    userIds.add(userId);
                } else {
                    int indexOfClickedUser = userIds.indexOf(userId);
                    chatListModelList.set(indexOfClickedUser, chatListModel);
                }

                chatListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatListActivity.this, getString(R.string.failed_to_fetch_chat_list, databaseError.getMessage())
                        , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        query.removeEventListener(childEventListener);
    }

}
