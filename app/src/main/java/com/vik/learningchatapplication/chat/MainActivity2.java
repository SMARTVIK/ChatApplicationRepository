package com.vik.learningchatapplication.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vik.learningchatapplication.R;
import com.vik.learningchatapplication.common.User;
import com.vik.learningchatapplication.common.Util;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = Util.getDataFromStorage(getApplicationContext());
                Log.d("TAG", "onClick: "+user.name);
            }
        });
    }
}