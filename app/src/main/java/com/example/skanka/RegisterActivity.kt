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
    }

    /*
    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            //btn_image_select.setImageBitmap(bitmap)
            //btn_image_select.alpha = 0f

            //val bitmapDrawable = BitmapDrawable(bitmap)
            //btn_image_select.setBackgroundDrawable(bitmapDrawable)
        }
    }

     */

    //Create the user
    private fun performRegister() {
        val emailreg = userEmail.text.toString()
        val passwordreg = userPassword.text.toString()
        val username = userName.text.toString()

        if (emailreg.isEmpty() && passwordreg.isEmpty() && username.isEmpty() ) {
            Toast.makeText(this, "Skriv in en giltig epostadress och lösenord", Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this, "Användare registrerad", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Fel!, försök igen", Toast.LENGTH_LONG).show()
                }
            }

            .addOnFailureListener {
                Toast.makeText(this, "Skriv in en giltig epostadress", Toast.LENGTH_LONG).show()
            }

    }
}







