package com.example.jetaeader.domain.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

class MUser(
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = "",
    @get:PropertyName("display_name")
    @set:PropertyName("display_name")
    var displayName: String? = "",
    @get:PropertyName("avatar_url")
    @set:PropertyName("avatar_url")
    var avatarUrl: String? = "",
    var quote: String? = "",
    var profession: String? = ""
) : ModelDefault()
