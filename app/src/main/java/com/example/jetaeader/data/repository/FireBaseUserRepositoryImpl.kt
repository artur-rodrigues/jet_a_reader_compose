package com.example.jetaeader.data.repository

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.datasource.FireBaseUserDataSource
import com.example.jetaeader.domain.model.MUser
import com.example.jetaeader.domain.repository.FireBaseUserRepository
import javax.inject.Inject

class FireBaseUserRepositoryImpl @Inject constructor(
    private val dataSource: FireBaseUserDataSource
) : FireBaseUserRepository {

    override suspend fun logInUser(email: String, password: String): BackgroundResult<MUser> {
        return dataSource.logInUser(email, password)
    }

    override suspend fun createUserAuth(email: String, password: String): BackgroundResult<MUser> {
        return dataSource.createUserAuth(email, password)
    }

    override suspend fun createUpdateUserFireBase(user: MUser): BackgroundResult<String> {
        return dataSource.createUpdateUserFireBase(user)
    }
}