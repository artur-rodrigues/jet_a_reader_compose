package com.example.jetaeader.data.usecase

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.domain.repository.FireBaseBookRepository
import javax.inject.Inject

class InsertBookUseCase @Inject constructor(
    private val repository: FireBaseBookRepository
) {

    suspend fun saveBookFirebase(mBook: MBook): BackgroundResult<String> {
        return repository.saveBookFirebase(mBook)
    }

    suspend fun updateBookFireBase(mBook: MBook): BackgroundResult<Unit> {
        return repository.updateBookFireBase(mBook)
    }
}