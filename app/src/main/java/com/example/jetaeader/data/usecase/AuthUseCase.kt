package com.example.jetaeader.data.usecase

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MUser
import com.example.jetaeader.domain.repository.FireBaseUserRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: FireBaseUserRepository
) {

    suspend fun logInUser(email: String, password: String): BackgroundResult<MUser> {
        return repository.logInUser(email, password)
    }

    suspend fun createUserAuth(email: String, password: String): BackgroundResult<MUser> {
        return repository.createUserAuth(email, password)
    }
}