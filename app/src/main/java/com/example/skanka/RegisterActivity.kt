package com.example.skanka

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.skanka.model.User
import com.google.firebase.auth.ActionCodeUrl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var firestoreDB: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firestoreDB = FirebaseFirestore.getInstance()

        btn_register.setOnClickListener {
            performRegister()
        }

        //Select image for the register user
        btn_image_select.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            //btn_image_select.setImageBitmap(bitmap)
            btn_image_select.alpha = 0f

            //val bitmapDrawable = BitmapDrawable(bitmap)
            //btn_image_select.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister() {
        val emailreg = userEmail.text.toString()
        val passwordreg = userPassword.text.toString()
        val username = userName.text.toString()

        if (emailreg.isEmpty() && passwordreg.isEmpty() && username.isEmpty() ) {
            Toast.makeText(this, "Please enter in email/pw/username", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailreg, passwordreg)
            .addOnCompleteListener { Task ->
                if (Task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val uid = user!!.uid
                    val username = User(username)
                    firestoreDB = FirebaseFirestore.getInstance()
                    firestoreDB.collection("users").document(uid).set(username)
                    startActivity(Intent(this, LoginActivity::class.java))
                    Toast.makeText(this, "Successfully registered", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error, try again", Toast.LENGTH_LONG).show()
                }

                //uploadImageToFirebaseStorage()
            }

            .addOnFailureListener {
                Toast.makeText(this, "Please enter a correct email/pw", Toast.LENGTH_LONG).show()
            }

    }
/*
    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/profileimg/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Register", "Success: ${it.metadata?.path}")

                saveUserToFirebase(it.toString())
            }
    }

    private fun saveUserToFirebase(profileImageUrl: String = "") {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")

        val user = User(uid, userName.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this, PostsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }

 */


}







