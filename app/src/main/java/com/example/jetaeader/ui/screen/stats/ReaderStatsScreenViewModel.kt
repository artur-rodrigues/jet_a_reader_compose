package com.example.jetaeader.ui.screen.stats

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
class ReaderStatsScreenViewModel @Inject constructor(
    private val useCase: GetBooksUseCase
) : ReaderViewModel("READER_STATS_VIEW_MODEL") {

    private val _data: MutableState<BackgroundResult<List<MBook?>>> = mutableStateOf(
        BackgroundResult.Idle)
    val data: State<BackgroundResult<List<MBook?>>> = _data


    fun getAllBooksFromFireBase() {
        launch(_data) {
            useCase.getAllBooksFromFireBase()
        }
    }
}