package com.example.skanka

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.skanka.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestore.getInstance
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_desc.*
import kotlinx.android.synthetic.main.new_post.*
import kotlinx.android.synthetic.main.new_post.etHeadLine
import kotlinx.android.synthetic.main.new_post.view.*

private lateinit var firestoreDb: FirebaseFirestore


class DescActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desc)
        firestoreDb = getInstance()


        //DocumentId containing all of below information
        var documentId = intent.getStringExtra("DocumentId")
        println("${documentId}")


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

                    val imageView: ImageView = findViewById(R.id.itemImage)
                    imageView.getImageUrl = post?.imageUrl

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

    //USER SELECT A POST
    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.cbGet -> {
                    if (checked) {
                        //Dimm the post
                        tvusername.text.toString()
                    } else {
                        //Let it be
                    }
                }
            }
        }
    }

}