package com.vik.learningchatapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.vik.learningchatapplication.common.NodeNames
import java.util.jar.Manifest

class SignUpActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION: Int = 100
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var name: EditText
    private lateinit var button: Button
    private lateinit var imageView: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var fileStorage: StorageReference
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var localFileUri: Uri
    private lateinit var serverFileUri: Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        email = findViewById(R.id.email)
        name = findViewById(R.id.name)
        password = findViewById(R.id.password)
        button = findViewById(R.id.signup)
        imageView = findViewById(R.id.ivProfilePicture)
        imageView.setOnClickListener {
            pickImage()
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if(it.resultCode == Activity.RESULT_OK) {
                    localFileUri = it.data?.data!!
                    imageView.setImageURI(localFileUri)
                }
        }

        auth = FirebaseAuth.getInstance()
        fileStorage = FirebaseStorage.getInstance().reference
        button.setOnClickListener {
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                if(it.isSuccessful) {
                    currentUser = FirebaseAuth.getInstance().currentUser!!
                    Log.d(Companion.TAG, "SignUp Successful: ${currentUser.uid}")
                    if (this::localFileUri.isInitialized && localFileUri != null) {
                        updateNameAndPhoto()
                    } else {
                        updateOnlyName()
                    }
                } else {
                    Log.d(TAG, "SignUp Failed: ${it.exception?.message}")
                }
            }
        }
    }

    private fun pickImage() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION)
                return
            }
        }
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage()
        } else {
            Toast.makeText(this, "Need this permission to select image!!", Toast.LENGTH_SHORT)
                .show()
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

    private fun updateNameAndPhoto() {
        val strFileName = currentUser.uid + ".jpg"
        val fileRef = fileStorage.child("images/$strFileName")
        fileRef.putFile(localFileUri).addOnCompleteListener {
            if (it.isSuccessful) {
                fileRef.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "updateNameAndPhoto: received the server uri")
                    serverFileUri = it
                    val profileUpdateRequest =
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(name.text.toString())
                            .setPhotoUri(serverFileUri)
                            .build()
                    currentUser.updateProfile(profileUpdateRequest).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d(TAG, "updateNameAndPhoto: upload profile successful")
                            val currentUserId = currentUser.uid
                            databaseReference =
                                FirebaseDatabase.getInstance().reference.child(NodeNames.USERS)
                            val map = HashMap<String, String>()
                            map.put(NodeNames.NAME, name.text.toString())
                            map.put(NodeNames.EMAIL, email.text.toString())
                            map.put(NodeNames.ONLINE, "true")
                            map.put(NodeNames.PHOTO, serverFileUri.path.toString())
                            databaseReference.child(currentUserId).setValue(map).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Log.d(TAG, "updateNameAndPhoto: database insertion successful")
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
            }
        }
    }

    companion object {
        private const val TAG = "SignUpActivity"
    }
}