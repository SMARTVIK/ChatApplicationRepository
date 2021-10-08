package com.vik.learningchatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignInAnonymously : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_anonymously)
        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().reference.child(NodeNames.USERS)
        findViewById<Button>(R.id.button3).setOnClickListener {
            createUser()
        }
    }

    private fun createUser() {
        auth.createUserWithEmailAndPassword("vps631532@gmail.com", "12345678")
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
            UserProfileChangeRequest.Builder().setDisplayName("Vivek Pratap Singh").build()
        currentUser.updateProfile(profileUpdateRequest).addOnCompleteListener {
            if (it.isSuccessful) {
                val currentUserId = currentUser.uid
                databaseReference =
                    FirebaseDatabase.getInstance().reference.child(NodeNames.USERS)
                val map = HashMap<String, String>()
                map.put(NodeNames.NAME, "Vivek Pratap Singh")
                map.put(NodeNames.LANGUAGE, "English")
                map.put(NodeNames.ONLINE, "true")
                map.put(NodeNames.LANGUAGE_CODE, "en")
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
        private const val TAG = "SignInAnonymously"
    }
}