package com.example.skanka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import io.opencensus.tags.Tag
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        buttonLogin.setOnClickListener {
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Email/password cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // Firebase auth check
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sucess!", Toast.LENGTH_LONG).show()
                    goPostsActivity()
                } else {

                    Toast.makeText(this, "Auth failed", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    private fun goPostsActivity() {

        val intent = Intent(this, PostsActivity::class.java)
        startActivity(intent)
    }
}
