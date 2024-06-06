package com.example.jetaeader.data.datasource

import com.example.jetaeader.data.api.firebase.FireBaseUtils
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.datasource.FireBaseBookDataSource
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.util.GOOGLE_BOOK_ID
import com.example.jetaeader.util.ID
import com.example.jetaeader.util.USER_ID
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class FireBaseBookDataSourceImpl @Inject constructor(
    private val collection: CollectionReference
) : FireBaseBookDataSource {

    override suspend fun getAllBooksFromDataBase(): BackgroundResult<List<MBook?>> {
        return FireBaseUtils.getDocuments(collection, USER_ID, FireBaseUtils.getUserId(), MBook::class.java)
    }

    override suspend fun getBookFromDataBase(bookId: String): BackgroundResult<MBook?> {
        return FireBaseUtils.getDocument(collection, ID, bookId, MBook::class.java)
    }

    override suspend fun saveBookFirebase(mBook: MBook): BackgroundResult<String> {
        mBook.userId = FireBaseUtils.getUserId()
        return FireBaseUtils.createUpdateDataFireBase(mBook, GOOGLE_BOOK_ID, mBook.googleBookId, MBook::class.java, collection)
    }

    override suspend fun updateBookFireBase(mBook: MBook): BackgroundResult<Unit> {
        return FireBaseUtils.updateDocument(collection, mBook)
    }

    override suspend fun deleteBookFireBase(mBook: MBook): BackgroundResult<Unit> {
        return FireBaseUtils.deleteDocument(collection, mBook)
    }

}