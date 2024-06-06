package com.example.jetaeader.data.usecase

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MUser
import com.example.jetaeader.domain.repository.FireBaseUserRepository
import javax.inject.Inject

class InsertUserUseCase  @Inject constructor(
    private val repository: FireBaseUserRepository
) {

    suspend fun createUpdateUserFireBase(user: MUser): BackgroundResult<String> {
        return repository.createUpdateUserFireBase(user)
    }
}