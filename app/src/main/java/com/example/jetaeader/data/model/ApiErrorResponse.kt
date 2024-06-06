package com.example.jetaeader.data.model

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("cod")
    val code: String,
    val message: String,
)