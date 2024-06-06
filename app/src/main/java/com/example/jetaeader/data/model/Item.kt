package com.example.jetaeader.data.model

import retrofit2.http.Field

data class Item(
    val accessInfo: AccessInfo,
    @Field("etag")
    val eTag: String,
    val id: String,
    val kind: String,
    val saleInfo: SaleInfo,
    val searchInfo: SearchInfo,
    val selfLink: String,
    val volumeInfo: VolumeInfo
)