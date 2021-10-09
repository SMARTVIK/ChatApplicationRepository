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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vik.learningchatapplication.common.NodeNames

class SignInAnonymously : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var email: EditText
    private lateinit var name: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_anonymously)
        auth = Firebase.auth
        email = findViewById(R.id.email)
        name = findViewById(R.id.name)
        databaseReference = FirebaseDatabase.getInstance().reference.child(NodeNames.USERS)
        findViewById<Button>(R.id.signup).setOnClickListener {
            createUser()
        }
    }

    private fun createUser() {
        auth.createUserWithEmailAndPassword(email.text.toString(), "12345678")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success")
                    currentUser = auth.currentUser!!
                    updateOnlyName()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateOnlyName() {
        val profileUpdateRequest =
            UserProfileChangeRequest.Builder().setDisplayName(name.text.toString()).build()
        currentUser.updateProfile(profileUpdateRequest).addOnCompleteListener {
            if (it.isSuccessful) {
                val currentUserId = currentUser.uid
                databaseReference =
                    FirebaseDatabase.getInstance().reference.child(NodeNames.USERS)
                val map = HashMap<String, String>()
                map.put(NodeNames.NAME, name.text.toString())
                map.put(NodeNames.LANGUAGE, "English")
                map.put(NodeNames.ONLINE, "true")
                map.put(NodeNames.LANGUAGE_CODE, "en")
                databaseReference.child(currentUserId).setValue(map).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "UpdateOnlyName Successful: ${currentUser.displayName}")
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
        private const val TAG = "SignInAnonymously"
    }
}