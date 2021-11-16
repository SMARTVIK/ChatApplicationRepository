package com.vik.learningchatapplication.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vik.learningchatapplication.R;
import com.vik.learningchatapplication.common.User;
import com.vik.learningchatapplication.common.Util;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.button).setOnClickListener(v -> {
            User user = new User("Vivek");
            Util.saveDataInStorage(getApplicationContext(), user);
            Log.d("TAG", "onClick: Data saved stored successfully");
            startActivity(new Intent(MainActivity.this, MainActivity2.class));
        });
    }
}