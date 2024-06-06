package com.example.jetaeader.ui.screen.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.data.usecase.GetBooksUseCase
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.ui.screen.ReaderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReaderSearchScreenViewModel @Inject constructor(
    private val useCase: GetBooksUseCase
) : ReaderViewModel("READER_SEARCH_VIEW_MODEL") {

    private val _resultState: MutableState<BackgroundResult<List<MBook>>> = mutableStateOf(BackgroundResult.Idle)
    val resultState: State<BackgroundResult<List<MBook>>> = _resultState

    init {
        searchBook("Android")
    }

    fun searchBook(query: String) {
        launch(_resultState) {
            useCase.getAllBooks(query)
        }
    }
}