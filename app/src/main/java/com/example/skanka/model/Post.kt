package com.example.skanka.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Post(
    var headline: String = "",
    var description: String = "",
    @get:PropertyName("image_url") @set:PropertyName("image_url") var imageUrl: String? = "",
    @get:PropertyName("creation_time_ms") @set:PropertyName("creation_time_ms") var creationTimeMs: Long = 0,
    var user: User? = null,
    @DocumentId
    var documentId: String = "",
    var taken: String = ""
)