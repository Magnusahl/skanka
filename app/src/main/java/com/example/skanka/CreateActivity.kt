package com.example.skanka

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.skanka.model.Post
import com.example.skanka.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create.*

private const val TAG = "ProfileActivity"
private const val PICK_PHOTO_CODE = 1234

class CreateActivity : AppCompatActivity() {
    private var photoUri: Uri? = null
    private var signedInUser: User? = null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        storageReference = FirebaseStorage.getInstance().reference
        firestoreDb = FirebaseFirestore.getInstance()
        firestoreDb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
                println("Signed in user: $signedInUser")
            }
            .addOnFailureListener { exception ->
                Log.i(TAG, "Failure fetching signed in user", exception)
            }

        //User can choose a image
        btnPickImage.setOnClickListener {
            Log.i(TAG, "Open up image picker on device")
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type = "image/*"
            if (imagePickerIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(imagePickerIntent, PICK_PHOTO_CODE)
            }
        }

        //Creates the post
        btnSubmit.setOnClickListener {
            handleSubmitButtonClick()
        }
    }

    //Send back msg if the user dosent fill in all of the required fields
    private fun handleSubmitButtonClick() {
        if (photoUri == null) {
            Toast.makeText(this, "Välj en bild", Toast.LENGTH_LONG).show()
            return
        }
        if (etDescription.text!!.isBlank()) {
            Toast.makeText(this, "Beskrivning kan inte vara tom", Toast.LENGTH_LONG).show()
            return
        }
        if (signedInUser == null) {
            Toast.makeText(this, "No signed in user, please wait", Toast.LENGTH_LONG).show()
            return
        }

        btnSubmit.isEnabled = false
        val photoUploadUri = photoUri as Uri
        val photoReference =
            storageReference.child("images/${System.currentTimeMillis()}-photo.jpg}")
        //Upload photo to Firebase storage
        photoReference.putFile(photoUploadUri)
            .continueWithTask { photoUploadTask ->
                Log.i(TAG, "upload bytes: ${photoUploadTask.result?.bytesTransferred}")
                //Retrive image url of the uploaded image
                photoReference.downloadUrl
            }.continueWithTask { downloadUrlTask ->

                //Create a post object
                val post = Post(
                    etHeadLine.text.toString(),
                    etDescription.text.toString(),
                    downloadUrlTask.result.toString(),
                    System.currentTimeMillis(),
                    signedInUser,
                    documentId = "",
                    taken = ""
                )
                firestoreDb.collection("posts").add(post)
            }.addOnCompleteListener { postCreationTask ->
                btnSubmit.isEnabled = true
                if (!postCreationTask.isSuccessful) {
                    Log.e(TAG, "Exception during Firebase operations", postCreationTask.exception)
                    Toast.makeText(this, "Failed to save post", Toast.LENGTH_LONG).show()
                }
                etDescription.text!!.clear()
                imageView.setImageResource(0)
                Toast.makeText(this, "Skänkt!", Toast.LENGTH_LONG).show()
                val profileIntent = Intent(this, PostsActivity::class.java)
                startActivity(profileIntent)
                finish()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Save the image from the imageview
        if (requestCode == PICK_PHOTO_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                photoUri = data?.data
                Log.i(TAG, "photouri $photoUri")
                imageView.setImageURI(photoUri)
            }
        }
    }
}


