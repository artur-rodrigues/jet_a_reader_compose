package com.example.jetaeader.domain.repository

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.data.model.BookResponse
import com.example.jetaeader.data.model.Item

interface BookRepository {

    suspend fun getAllBooks(query: String) : BackgroundResult<BookResponse>

    suspend fun getBookInfo(bookId: String) : BackgroundResult<Item>
}