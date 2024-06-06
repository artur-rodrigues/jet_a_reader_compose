package com.example.jetaeader.data.model

data class BookResponse(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)