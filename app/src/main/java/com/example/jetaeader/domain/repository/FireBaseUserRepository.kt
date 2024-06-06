package com.example.jetaeader.domain.repository

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MUser

interface FireBaseUserRepository {

    suspend fun logInUser(email: String, password: String): BackgroundResult<MUser>

    suspend fun createUserAuth(email: String, password: String): BackgroundResult<MUser>

    suspend fun createUpdateUserFireBase(user: MUser): BackgroundResult<String>
}