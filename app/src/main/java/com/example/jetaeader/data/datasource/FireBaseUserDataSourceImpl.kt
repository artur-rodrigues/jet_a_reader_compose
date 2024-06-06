package com.example.jetaeader.data.datasource

import com.example.jetaeader.data.api.firebase.FireBaseUtils
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.datasource.FireBaseUserDataSource
import com.example.jetaeader.domain.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseUserDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val collection: CollectionReference
) : FireBaseUserDataSource {

    override suspend fun logInUser(email: String, password: String): BackgroundResult<MUser> {
        return FireBaseUtils.authFireBase(collection) {
            auth.signInWithEmailAndPassword(email, password).await()
        }
    }

    override suspend fun createUserAuth(email: String, password: String): BackgroundResult<MUser> {
        return FireBaseUtils.authFireBase(collection) {
            auth.createUserWithEmailAndPassword(email, password).await()
        }
    }

    override suspend fun createUpdateUserFireBase(user: MUser): BackgroundResult<String> {
        return FireBaseUtils.createUpdateDataFireBase(user, collection = collection)
    }
}