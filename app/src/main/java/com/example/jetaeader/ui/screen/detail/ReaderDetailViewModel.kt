package com.example.jetaeader.ui.screen.detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.data.usecase.GetBooksUseCase
import com.example.jetaeader.data.usecase.InsertBookUseCase
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.ui.screen.ReaderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReaderDetailViewModel @Inject constructor(
    private val useCase: GetBooksUseCase,
    private val insertUseCase: InsertBookUseCase
) : ReaderViewModel("READER_DETAIL_VIEW_MODEL") {

    private val _resultGetState: MutableState<BackgroundResult<MBook>> = mutableStateOf(BackgroundResult.Idle)
    val resultGetState: State<BackgroundResult<MBook>> = _resultGetState

    private val _resultCreateState: MutableState<BackgroundResult<String>> = mutableStateOf(BackgroundResult.Idle)
    val resultCreateState: State<BackgroundResult<String>> = _resultCreateState

    fun getBook(bookId: String) {
        launch(_resultGetState) {
            useCase.getBook(bookId)
        }
    }

    fun saveBookFirebase(book: MBook) {
        launch(_resultCreateState) {
            insertUseCase.saveBookFirebase(book)
        }
    }
}