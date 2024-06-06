package com.example.jetaeader.ui.screen.update

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.data.usecase.DeleteBookUseCase
import com.example.jetaeader.data.usecase.GetBooksUseCase
import com.example.jetaeader.data.usecase.InsertBookUseCase
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.ui.screen.ReaderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReaderUpdateScreenViewModel @Inject constructor(
    private val getUseCase: GetBooksUseCase,
    private val insertBookUseCase: InsertBookUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) : ReaderViewModel("UPDATE_BOOK_VIEW_MODEL") {

    private val _result: MutableState<BackgroundResult<MBook?>> = mutableStateOf(BackgroundResult.Idle)
    val result: State<BackgroundResult<MBook?>> = _result

    private val _resultUpdate: MutableState<BackgroundResult<Unit>> = mutableStateOf(BackgroundResult.Idle)
    val resultUpdate: State<BackgroundResult<Unit>> = _resultUpdate

    private val _resultDelete: MutableState<BackgroundResult<Unit>> = mutableStateOf(BackgroundResult.Idle)
    val resultDelete: State<BackgroundResult<Unit>> = _resultDelete

    fun getBookFromFireBase(bookId: String) {
        launch(_result) {
            getUseCase.getBookFromFireBase(bookId)
        }
    }

    fun updateBookInFireBase(book: MBook) {
        launch(_resultUpdate) {
            insertBookUseCase.updateBookFireBase(book)
        }
    }

    fun deleteBookFromFireBase(book: MBook) {
        launch(_resultDelete) {
            deleteBookUseCase.deleteBookFireBase(book)
        }
    }
}