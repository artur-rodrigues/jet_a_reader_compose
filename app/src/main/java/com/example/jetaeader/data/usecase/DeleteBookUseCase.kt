package com.example.jetaeader.data.usecase

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.domain.repository.FireBaseBookRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(
    private val repository: FireBaseBookRepository
) {

    suspend fun deleteBookFireBase(mBook: MBook): BackgroundResult<Unit> {
        return repository.deleteBookFireBase(mBook)
    }
}