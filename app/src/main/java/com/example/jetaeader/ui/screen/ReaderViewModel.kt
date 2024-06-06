package com.example.jetaeader.ui.screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetaeader.data.model.BackgroundResult
import kotlinx.coroutines.launch

abstract class ReaderViewModel(private val errorTag: String) : ViewModel() {

    fun <T> launch(
        state: MutableState<BackgroundResult<T>>,
        execute: suspend () -> BackgroundResult<T>
    ) {
        state.value = BackgroundResult.Loading

        viewModelScope.launch {
            try {
                val result = execute()

                if (result is BackgroundResult.Error) {
                    Log.e(errorTag, "searchBook: ${ result.error?.message ?: result.exception.message }", result.exception)
                }

                state.value = result
            } catch (e: Exception) {
                state.value = BackgroundResult.Error(e)
            }
        }
    }
}