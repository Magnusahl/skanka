package com.example.skanka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        btn_register.setOnClickListener {
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()
        }
    }
}
