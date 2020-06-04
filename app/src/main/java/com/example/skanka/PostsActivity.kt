package com.example.skanka

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skanka.adapters.PostsAdapter
import com.example.skanka.model.Post
import com.example.skanka.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_desc.*
import kotlinx.android.synthetic.main.activity_posts.*

private const val TAG = "ProfileActivity"
const val EXTRA_USERNAME = "EXTRA_USERNAME"
open class PostsActivity : AppCompatActivity(), PostsAdapter.OnItemClickListener {

    private var signedInUser: User? = null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var posts: MutableList<Post>
    private lateinit var adapter: PostsAdapter
    private var gridLayoutManager: GridLayoutManager? = null

    //Pass the data from adapter in to the detail activity view
    override fun onItemClick(post: Post) {
        val intent = Intent(this@PostsActivity,DescActivity::class.java)
        intent.putExtra("DocumentId", post.documentId)
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        posts = mutableListOf()
        adapter = PostsAdapter(this ,posts, this)
        rvPosts.adapter = adapter
        //Order the posts in a grid
        gridLayoutManager = GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        rvPosts.layoutManager = gridLayoutManager
        firestoreDb = FirebaseFirestore.getInstance()

        //Sign in user
        firestoreDb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
                println("Signed in user: $signedInUser")
            }
            .addOnFailureListener{ exception ->
                Log.i(TAG,"Failure fetching signed in user", exception)
            }

        //Order posts by creating time
        var postsReference = firestoreDb
            .collection("posts")
            //.limit(20)
            .orderBy("creation_time_ms", Query.Direction.DESCENDING)

        //Show user profile name
        val user = intent.getStringExtra(EXTRA_USERNAME)
        if (user != null) {
            supportActionBar?.title = user
            postsReference = postsReference.whereEqualTo("user.userName", user)
        }

        //Show posts in a list
        postsReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(Post::class.java)
            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()
            for (post in postList) {
                println("!!Datacreated ${post.documentId}")
            }
        }

        //FAB button to the create activity
        fabCreate.setOnClickListener{
            val intent = Intent( this, CreateActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile) {
            val intent = Intent(this, ProfileActivity::class.java)
            //See signed in users username
            intent.putExtra(EXTRA_USERNAME, signedInUser?.userName)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
