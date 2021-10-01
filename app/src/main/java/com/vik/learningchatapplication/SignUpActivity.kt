package com.vik.learningchatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.lang.ref.PhantomReference

class SignUpActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var name: EditText
    private lateinit var button: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        email = findViewById(R.id.email)
        name = findViewById(R.id.name)
        password = findViewById(R.id.password)
        button = findViewById(R.id.signup)
        auth = FirebaseAuth.getInstance()
        button.setOnClickListener {
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                if(it.isSuccessful) {
                    currentUser = FirebaseAuth.getInstance().currentUser!!
                    Log.d(Companion.TAG, "SignUp Successful: ${currentUser.uid}")
                    updateOnlyName()
                } else {
                    Log.d(TAG, "SignUp Failed: ${it.exception?.message}")
                }
            }
        }
    }

    private fun updateOnlyName() {
        val profileUpdateRequst =
            UserProfileChangeRequest.Builder().setDisplayName(name.text.toString()).build()
        currentUser.updateProfile(profileUpdateRequst).addOnCompleteListener {
            if (it.isSuccessful) {
                val currentUserId = currentUser.uid
                databaseReference =
                    FirebaseDatabase.getInstance().reference.child(NodeNames.USERS)
                val map = HashMap<String, String>()
                map.put(NodeNames.NAME, name.text.toString())
                map.put(NodeNames.EMAIL, email.text.toString())
                map.put(NodeNames.ONLINE, "true")
                map.put(NodeNames.PHOTO, "")
                databaseReference.child(currentUserId).setValue(map).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "UpdateOnlyName Successful: ${currentUser.displayName}")
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.d(TAG, "updateOnlyName Failed: ${it.exception?.message}")
                    }
                }
            } else {
                Log.d(TAG, "updateOnlyName: ${it.exception?.message}")
            }
        }
    }

    companion object {
        private const val TAG = "SignUpActivity"
    }
}