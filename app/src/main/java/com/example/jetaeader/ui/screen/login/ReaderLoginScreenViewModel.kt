package com.example.jetaeader.ui.screen.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.data.usecase.AuthUseCase
import com.example.jetaeader.domain.model.MUser
import com.example.jetaeader.ui.screen.ReaderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReaderLoginScreenViewModel @Inject constructor(
    private val useCase: AuthUseCase
) : ReaderViewModel("READER_LOGIN_VIEW_MODEL") {

    private val _result: MutableState<BackgroundResult<MUser>> = mutableStateOf(BackgroundResult.Idle)
    val result: State<BackgroundResult<MUser>> = _result

    fun signInUserAuth(email: String, password: String) {
        launch(_result) {
            useCase.logInUser(email, password)
        }
    }

    fun createUserAuth(email: String, password: String) {
        launch(_result) {
            useCase.createUserAuth(email, password)
        }
    }
}