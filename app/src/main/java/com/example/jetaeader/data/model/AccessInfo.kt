package com.example.jetaeader.data.model

import retrofit2.http.Field

data class AccessInfo(
    val accessViewStatus: String,
    val country: String,
    val embeddable: Boolean,
    val epub: Epub,
    val pdf: Pdf,
    val publicDomain: Boolean,
    val quoteSharingAllowed: Boolean,
    val textToSpeechPermission: String,
    @Field("viewability")
    val viewAbility: String,
    val webReaderLink: String
)