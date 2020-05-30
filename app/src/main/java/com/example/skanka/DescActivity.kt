package com.example.skanka

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.skanka.model.Post
import com.example.skanka.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestore.getInstance
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_desc.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.new_post.*
import kotlinx.android.synthetic.main.new_post.etHeadLine
import kotlinx.android.synthetic.main.new_post.view.*

private lateinit var firestoreDb: FirebaseFirestore


class DescActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desc)
        firestoreDb = getInstance()


        //Show delete button
        if (userName != null) {
            btnDelete.setVisibility(View.VISIBLE)
        } else {
            btnDelete.setVisibility(View.INVISIBLE)
        }

        //DocumentId containing all of below information
        var documentId = intent.getStringExtra("DocumentId")
        println("${documentId}")

        //Users
        fun loadUsers() {
            firestoreDb.collection("posts")
                .document(documentId)
                .get().addOnSuccessListener { document ->
                    if (document != null) {
                        val post = document.toObject(Post::class.java)

                        val tvDescUsername: TextView = findViewById(R.id.tvDescUsername)
                        tvDescUsername.text = post?.user.toString()
                        println("user ${post?.user}")
                    }
                }
        }

        //USER SELECT A POST
        btnGet.setOnClickListener {
            loadUsers()
        }


        //Post loaded from Postactivity
        firestoreDb.collection("posts")
            .document(documentId)
            .get().addOnSuccessListener { document ->
                if (document != null) {
                    val post = document.toObject(Post::class.java)

                    val etDescHeadLine: TextView = findViewById(R.id.etDescHeadLine)
                    etDescHeadLine.text = post?.headline

                    val etDescription: TextView = findViewById(R.id.etDescDescription)
                    etDescription.text = post?.description

                    //val imageView: ImageView = findViewById(R.id.itemImage)
                    //imageView.getImageUrl = post?.imageUrl

                    println("Postloaded${post?.headline}")
                }
            }

        //DELETE POST
        fun deleteData() {
            firestoreDb
                .collection("posts")
                .document(documentId)
                .delete()
        }

        btnDelete.setOnClickListener {
            deleteData()
            finish()
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
        }
    }

}