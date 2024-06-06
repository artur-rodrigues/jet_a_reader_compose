package com.example.jetaeader.data.repository

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.datasource.FireBaseBookDataSource
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.domain.repository.FireBaseBookRepository
import javax.inject.Inject

class FireBaseBookRepositoryImpl @Inject constructor(
    private val fireBaseBookDataSource: FireBaseBookDataSource
) : FireBaseBookRepository {

    override suspend fun getAllBooksFromDataBase(): BackgroundResult<List<MBook?>> {
        return fireBaseBookDataSource.getAllBooksFromDataBase()
    }

    override suspend fun getBookFromDataBase(bookId: String): BackgroundResult<MBook?> {
        return fireBaseBookDataSource.getBookFromDataBase(bookId)
    }

    override suspend fun saveBookFirebase(mBook: MBook): BackgroundResult<String> {
        return fireBaseBookDataSource.saveBookFirebase(mBook)
    }

    override suspend fun updateBookFireBase(mBook: MBook): BackgroundResult<Unit> {
        return fireBaseBookDataSource.updateBookFireBase(mBook)
    }

    override suspend fun deleteBookFireBase(mBook: MBook): BackgroundResult<Unit> {
        return fireBaseBookDataSource.deleteBookFireBase(mBook)
    }
}