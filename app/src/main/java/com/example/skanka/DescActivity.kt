package com.example.skanka

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.skanka.model.Post
import com.example.skanka.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestore.getInstance
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_desc.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.new_post.*
import kotlinx.android.synthetic.main.new_post.etHeadLine
import kotlinx.android.synthetic.main.new_post.view.*
import kotlinx.android.synthetic.main.activity_desc.itemImage as itemImage1

private lateinit var firestoreDb: FirebaseFirestore
private var signedInUser: User? = null

class DescActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desc)
        firestoreDb = getInstance()

        //DocumentId containing all of below information
        val documentId = intent.getStringExtra("DocumentId")


        fun textGet(post: Post) {
            val etDescHeadLine: TextView = findViewById(R.id.etDescHeadLine)
            etDescHeadLine.text = post?.headline

            val etDescription: TextView = findViewById(R.id.etDescDescription)
            etDescription.text = post?.description
        }

        fun loadImageUrl(post: Post) {
            val imageurl = post.imageUrl
            //Glide Image
            Glide.with(this)
                .load(imageurl)
                .into(itemImage)
        }

        //USER SELECT A POST
        fun userTaken(post: Post) {
            btnGet.setOnClickListener {
                val tvDescUsername: TextView = findViewById(R.id.tvDescUsername)
                tvDescUsername.text = post.user.toString()

                firestoreDb.collection("posts")
                    .document(documentId)
                    .update("taken", FirebaseAuth.getInstance().currentUser?.uid)


            }
        }


        //Post loaded from Postactivity
            firestoreDb.collection("posts")
                .document(documentId)
                .get().addOnSuccessListener { document ->
                    if (document != null) {
                        val post = document.toObject(Post::class.java)
                        //Get Headline and description
                        textGet(post!!)
                        //Get imageUrl
                        loadImageUrl(post)

                        userTaken(post)

                        //Show delete button
                        if (signedInUser!! == post.user) {
                            btnDelete.setVisibility(View.VISIBLE)
                        } else {
                            btnDelete.setVisibility(View.INVISIBLE)
                        }
                    }
                }




        btnUndo.setOnClickListener {
            val tvDescUsername: TextView = findViewById(R.id.tvDescUsername)
            tvDescUsername.text = ""
        }


        //Load User to select post and write to firebase taken
        firestoreDb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)

                //updatePostTaken()

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