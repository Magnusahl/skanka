package com.example.skanka

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.skanka.model.Post

class DescActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desc)

        intent?.let {
            val post = intent.extras.getParcelable(PostsActivity) as Post
             = Post.toString()
        }

        //var HeadLine = intent.getStringExtra("HeadLine")
        //var Description = intent.getStringExtra("Description")
        //var imageUrl = intent.getStringExtra("imageUrl")

        //var etHeadLine:TextView = findViewById(R.id.etHeadLine)
        //var etDescription:TextView = findViewById(R.id.etDescription)
        //etHeadLine.text = title.toString()
        //etDescription.text = title.toString()
    }
}