package com.example.jetaeader.domain.model

import com.google.firebase.firestore.Exclude

abstract class ModelDefault {

    @Exclude
    var id: String? = null
}