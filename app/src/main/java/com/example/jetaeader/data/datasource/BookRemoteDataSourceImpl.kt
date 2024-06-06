package com.example.jetaeader.data.datasource

import com.example.jetaeader.data.api.BookApi
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.data.model.BookResponse
import com.example.jetaeader.data.model.Item
import com.example.jetaeader.domain.datasource.BookRemoteDataSource
import javax.inject.Inject

class BookRemoteDataSourceImpl @Inject constructor(
    private val bookApi: BookApi
) : BookRemoteDataSource {

    override suspend fun getAllBooks(query: String): BackgroundResult<BookResponse> {
        return bookApi.getAllBooks(query)
    }

    override suspend fun getBookInfo(bookId: String): BackgroundResult<Item> {
        return bookApi.getBookInfo(bookId)
    }
}