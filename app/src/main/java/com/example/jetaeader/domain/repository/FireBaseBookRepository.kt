package com.example.jetaeader.domain.repository

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MBook

interface FireBaseBookRepository {

    suspend fun getAllBooksFromDataBase(): BackgroundResult<List<MBook?>>

    suspend fun getBookFromDataBase(bookId: String): BackgroundResult<MBook?>

    suspend fun saveBookFirebase(mBook: MBook): BackgroundResult<String>

    suspend fun updateBookFireBase(mBook: MBook): BackgroundResult<Unit>

    suspend fun deleteBookFireBase(mBook: MBook): BackgroundResult<Unit>
}