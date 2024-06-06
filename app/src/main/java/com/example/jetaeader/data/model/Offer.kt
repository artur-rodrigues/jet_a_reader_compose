package com.example.jetaeader.data.model

import retrofit2.http.Field

data class Offer(
    @Field("finskyOfferType")
    val finskyOfferType: Int,
    @Field("giftable")
    val giftable: Boolean,
    val listPrice: ListPriceX,
    val retailPrice: RetailPrice
)