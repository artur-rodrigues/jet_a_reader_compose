package com.example.jetaeader.data.usecase

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.domain.model.converters.convertToSuccessMBook
import com.example.jetaeader.domain.model.converters.convertToSuccessMBookList
import com.example.jetaeader.domain.repository.BookRepository
import com.example.jetaeader.domain.repository.FireBaseBookRepository
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(
    private val booksRepository: BookRepository,
    private val fireBaseBookRepository: FireBaseBookRepository
) {

    suspend fun getAllBooksFromFireBase(): BackgroundResult<List<MBook?>> {
        return fireBaseBookRepository.getAllBooksFromDataBase()
    }

    suspend fun getBookFromFireBase(bookId: String): BackgroundResult<MBook?> {
        return fireBaseBookRepository.getBookFromDataBase(bookId)
    }

    suspend fun getAllBooks(query: String): BackgroundResult<List<MBook>> {
        return executeCall(booksRepository.getAllBooks(query)) {
            it.convertToSuccessMBookList()
        }
    }

    suspend fun getBook(bookId: String): BackgroundResult<MBook> {
        return executeCall(booksRepository.getBookInfo(bookId)) {
            it.convertToSuccessMBook()
        }
    }

    private fun <IN, OUT> executeCall(
        input: BackgroundResult<IN>,
        output: (BackgroundResult.Success<IN>) -> BackgroundResult<OUT>
    ): BackgroundResult<OUT> {
        return when (input) {
            is BackgroundResult.Error -> input
            BackgroundResult.Idle -> BackgroundResult.Idle
            BackgroundResult.Loading -> BackgroundResult.Loading
            is BackgroundResult.Success -> output(input)
        }
    }
}