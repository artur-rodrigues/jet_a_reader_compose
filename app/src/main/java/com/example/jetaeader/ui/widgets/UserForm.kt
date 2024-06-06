package com.example.jetaeader.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetaeader.R
import com.example.jetaeader.ui.components.InputField
import com.example.jetaeader.ui.components.SubmitButton
import com.example.jetaeader.util.isValidEmail

@Preview(showBackground = true)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = {s1, s2 ->}
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }

    val password = rememberSaveable {
        mutableStateOf("")
    }

    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }

    val passwordFocusRequest = FocusRequester()

    val keyboardController = LocalSoftwareKeyboardController.current

    val valid = remember(email.value, password.value) {
        email.value.trim().isValidEmail() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .height(300.dp)
        .background(color = MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (isCreateAccount) {
            Text(
                text = "Please enter a valid email and password that is at least 6 character",
                modifier = Modifier.padding(
                    start = 10.dp,
                    bottom = 10.dp,
                    end = 10.dp
                )
            )
        } else {
            Text(text = "")
        }

        EmailInput(
            emailState = email,
            enable = !loading,
            onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            }
        )

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                keyboardController?.hide()
            }
        )

        SubmitButton(
            textId = if(isCreateAccount) {
                "Create account"
            } else {
                "Login"
            },
            loading = loading,
            validInputs = valid
        ) {
            keyboardController?.hide()
            onDone(email.value.trim(), password.value.trim())
        }
    }
}


@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enable: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val emailFocusRequest = FocusRequester()

    var isError by remember(emailState.value) {
        mutableStateOf(false)
    }

    InputField(
        modifier = modifier
            .focusRequester(emailFocusRequest)
            .onFocusChanged {
                isError = if (it.isFocused) {
                    false
                } else {
                    !emailState.value
                        .trim()
                        .isValidEmail()
                }
            },
        valueState = emailState,
        labelId = labelId,
        enabled = enable,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(text = "Email is invalid")
            }
        }
    ) {
        if (isError) {
            Icon(painterResource(id = R.drawable.error),"Error", tint = MaterialTheme.colorScheme.error)
        }
    }

    LaunchedEffect(Unit) {
        emailFocusRequest.requestFocus()
    }
}

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    labelId: String = "Password",
    enabled: Boolean = true,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation = if (passwordVisibility.value) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    InputField(
        modifier = modifier,
        valueState = passwordState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction,
        visualTransformation = visualTransformation
    ) {
        PasswordVisibility(passwordVisibility = passwordVisibility)
    }
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value } ) {
        val iconId = if (passwordVisibility.value) R.drawable.ic_eye_closed else R.drawable.ic_eye_open
        Icon(
            modifier = Modifier.size(25.dp),
            painter = painterResource(id = iconId),
            contentDescription = "Password icon"
        )
    }
}