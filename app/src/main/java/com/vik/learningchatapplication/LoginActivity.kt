package com.vik.learningchatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var button: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        button = findViewById(R.id.button)
        auth = FirebaseAuth.getInstance()

        button.setOnClickListener {
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, ChatListActivity::class.java)
                        startActivity(intent)
                    } else {
                        if(it.exception?.message.toString().contains("no user record")) {
                            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, SignUpActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed!! ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}