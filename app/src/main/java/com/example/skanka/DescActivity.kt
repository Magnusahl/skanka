package com.example.skanka

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.skanka.model.Post
import com.example.skanka.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestore.getInstance
import kotlinx.android.synthetic.main.activity_desc.*
import kotlinx.android.synthetic.main.new_post.*
import kotlinx.android.synthetic.main.activity_desc.itemImage as itemImage1

private var signedInUser: User? = null

class DescActivity : AppCompatActivity() {
   private val firestoreDb = getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desc)

        //DocumentId containing all of below information
        val documentId = intent.getStringExtra("DocumentId")

        //Mark the post taken
        fun userTaken() {
            btnGet.setOnClickListener {
                val tvDescUsername: TextView = findViewById(R.id.tvDescUsername)
                tvDescUsername.text = "Bokad för hämtning"

                btnUndo.visibility = View.VISIBLE
                btnGet.visibility = View.INVISIBLE


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
                    userTaken()

                    firestoreDb.collection("users")
                        .document(FirebaseAuth.getInstance().currentUser?.uid as String)
                        .get()
                        .addOnSuccessListener { userSnapshot ->
                            signedInUser = userSnapshot.toObject(User::class.java)

                            if (signedInUser == post.user) {
                                btnDelete.visibility = View.VISIBLE
                            } else {
                                btnDelete.visibility = View.INVISIBLE
                            }

                            //Load User to select post and write to firebase taken
                            if (post.taken.isEmpty()) {
                                val tvDescUsername: TextView = findViewById(R.id.tvDescUsername)
                                tvDescUsername.text = ""
                                btnGet.visibility = View.VISIBLE
                            } else {
                                val tvDescUsername: TextView = findViewById(R.id.tvDescUsername)
                                tvDescUsername.text = "Bokad för hämtning"
                                btnUndo.visibility = View.VISIBLE
                                btnGet.visibility = View.INVISIBLE
                            }
                        }
                }
            }

        btnUndo.visibility = View.INVISIBLE

        //Delete post
        fun deleteData() {
            firestoreDb
                .collection("posts")
                .document(documentId)
                .delete()
        }

        btnDelete.setOnClickListener {
            deleteData()
            finish()
            Toast.makeText(this, "Raderad", Toast.LENGTH_SHORT).show()
        }


        btnUndo.setOnClickListener {
            firestoreDb.collection("posts")
                .document(documentId)
                .update("taken", "")

            btnUndo.visibility = View.INVISIBLE
            btnGet.visibility = View.VISIBLE

            val tvDescUsername: TextView = findViewById(R.id.tvDescUsername)
            tvDescUsername.text = ""
        }
    }

    //Load image from post activity
    private fun loadImageUrl(post: Post) {
        val imageurl = post.imageUrl
        //Glide Image
        Glide.with(this)
            .load(imageurl)
            .into(itemImage)
    }

    //Get the Headline and description from post activity
    private fun textGet(post: Post) {
        val etDescHeadLine: TextView = findViewById(R.id.etDescHeadLine)
        etDescHeadLine.text = post.headline

        val etDescription: TextView = findViewById(R.id.etDescDescription)
        etDescription.text = post.description
    }
}