package com.example.skanka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.userEmail
import kotlinx.android.synthetic.main.activity_login.userPassword
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            goPostsActivity()
        }

        buttonLogin.setOnClickListener {
            //buttonLogin.isEnabled = false
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Email/password cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // Firebase auth check
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                buttonLogin.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sucess!", Toast.LENGTH_LONG).show()
                    goPostsActivity()
                } else {
                    Toast.makeText(this, "Auth failed", Toast.LENGTH_LONG).show()
                }
            }
        }

        tv_register.setOnClickListener {
            goRegisterActivity()
        }
    }

    private fun goPostsActivity() {

        val intent = Intent(this, PostsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

}
