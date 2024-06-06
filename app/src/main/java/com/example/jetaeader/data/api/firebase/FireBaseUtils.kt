package com.example.jetaeader.data.api.firebase

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MUser
import com.example.jetaeader.domain.model.ModelDefault
import com.example.jetaeader.util.AT
import com.example.jetaeader.util.EMPTY_STRING
import com.example.jetaeader.util.ID
import com.example.jetaeader.util.NA
import com.example.jetaeader.util.USER_ID
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await

object FireBaseUtils {

    fun getUserId(): String {
        return getUser()?.uid ?: EMPTY_STRING
    }

    fun getUserName(): String {
        return getUser()?.email?.split(AT)?.get(0) ?: NA
    }

    fun getUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    suspend fun authFireBase(
        collection: CollectionReference,
        execute: suspend () -> AuthResult
    ): BackgroundResult<MUser> {
        return try {
            val result = execute()
            val displayName = result.user?.email?.split(AT)?.get(0)
            val id = result.user?.uid
            val user = MUser(userId = id, displayName = displayName)

            val retUserFireBase = createUpdateDataFireBase(user, USER_ID, id, MUser::class.java, collection)

            if(retUserFireBase is BackgroundResult.Success) {
                user.id = retUserFireBase.data
                BackgroundResult.Success(user)
            } else {
                BackgroundResult.Error((retUserFireBase as BackgroundResult.Error).exception)
            }


        } catch (e: Exception) {
            BackgroundResult.Error(e)
        }
    }

    suspend fun <INSERT : ModelDefault> createUpdateDataFireBase(
        t: INSERT,
        field: String? = null,
        value: String? = null,
        clazz: Class<INSERT>? = null,
        collection: CollectionReference
    ): BackgroundResult<String> {
        return try {
            val create = suspend {
                val createUserTask = collection.add(t).await()
                val id = createUserTask.id
                collection.document(id).update(ID, id).await()
                id
            }

            if (field != null && value != null && clazz != null) {
                val document: BackgroundResult<INSERT?> = getDocument(collection, field, value, clazz)

                if(document is BackgroundResult.Success && document.data != null) {
                    BackgroundResult.Success(document.data.id!!)
                } else {
                    BackgroundResult.Success(create())
                }
            } else {
                BackgroundResult.Success(create())
            }
        } catch (e: Exception) {
            BackgroundResult.Error(e)
        }
    }

    suspend fun <VALUE : ModelDefault> updateDocument(
        collection: CollectionReference,
        value: VALUE,
    ): BackgroundResult<Unit> {
        return updateDeleteDocument(value.id, collection) { it.set(value) }
    }

    suspend fun <VALUE : ModelDefault> deleteDocument(
        collection: CollectionReference,
        value: VALUE,
    ): BackgroundResult<Unit> {
        return updateDeleteDocument(value.id, collection) { it.delete() }
    }

    private suspend fun updateDeleteDocument(
        id: String?,
        collection: CollectionReference,
        firebaseExecute: (DocumentReference) -> Task<Void>
    ): BackgroundResult<Unit> {
        return try {
            if (id == null) {
                BackgroundResult.Error(Exception("Can't update value without id"))
            } else {
                firebaseExecute(collection.document(id)).await()
                BackgroundResult.Success(Unit)
            }
        } catch (e: Exception) {
            BackgroundResult.Error(e)
        }
    }

    suspend fun <RESULT> getDocument(
        collection: CollectionReference,
        field: String,
        value: String,
        clazz: Class<RESULT>
    ): BackgroundResult<RESULT?> {
        return when(val resultList = getDocuments(collection, field, value, clazz)) {
            is BackgroundResult.Error -> BackgroundResult.Error(resultList.exception)
            BackgroundResult.Idle -> BackgroundResult.Idle
            BackgroundResult.Loading -> BackgroundResult.Loading
            is BackgroundResult.Success -> {
                val data = if (resultList.data.isNotEmpty()) {
                    resultList.data[0]
                } else {
                    null
                }

                BackgroundResult.Success(data)
            }
        }
    }

    suspend fun <RESULT> getDocuments(
        collection: CollectionReference,
        field: String,
        value: String,
        clazz: Class<RESULT>
    ): BackgroundResult<List<RESULT?>> {
        return try {
            val result = collection.whereEqualTo(field, value).get().await()

            val returnObject = if (result.documents.size > 0) {
                result.documents.map {
                    it.toObject(clazz)
                }
            } else {
                emptyList()
            }

            BackgroundResult.Success(returnObject)
        } catch (e: Exception) {
            BackgroundResult.Error(e)
        }
    }

    suspend fun <T> getAllDocuments(collection: CollectionReference, clazz: Class<T>): BackgroundResult<List<T?>> {
        return try {
            val task = collection.get().await()
            val list = task.documents.map { snapShot ->
                snapShot.toObject(clazz)
            }
            BackgroundResult.Success(list)
        } catch (e: Exception) {
            BackgroundResult.Error(e)
        }
    }
}