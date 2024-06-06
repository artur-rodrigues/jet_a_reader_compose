package com.example.jetaeader.data.repository

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.data.model.BookResponse
import com.example.jetaeader.data.model.Item
import com.example.jetaeader.domain.datasource.BookRemoteDataSource
import com.example.jetaeader.domain.repository.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val remoteDataSource: BookRemoteDataSource
) : BookRepository {

    override suspend fun getAllBooks(query: String): BackgroundResult<BookResponse> {
        return remoteDataSource.getAllBooks(query)
    }

    override suspend fun getBookInfo(bookId: String): BackgroundResult<Item> {
        return remoteDataSource.getBookInfo(bookId)
    }

}