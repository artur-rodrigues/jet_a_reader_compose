package com.example.jetaeader.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.ui.components.ReaderLogo
import com.example.jetaeader.ui.components.VerticalSpacer
import com.example.jetaeader.ui.navigations.ReaderScreens
import com.example.jetaeader.ui.widgets.UserForm
import com.example.jetaeader.util.navigateRemoveSource
import kotlinx.coroutines.delay

@Composable
fun ReaderLoginScreen(
    navController: NavController,
    viewModel: ReaderLoginScreenViewModel
) {
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    val result = remember {
        viewModel.result
    }

    val loading = remember(result.value) {
        mutableStateOf(result.value == BackgroundResult.Loading)
    }

    result.value.run {
        when(this) {
            is BackgroundResult.Success -> LaunchedEffect(Unit) {
                delay(200)
                navController.navigateRemoveSource(ReaderScreens.LoginScreen, ReaderScreens.HomeScreen)
            }
            is BackgroundResult.Error -> Toast.makeText(LocalContext.current, exception.message, Toast.LENGTH_SHORT).show()
            BackgroundResult.Idle -> Unit
            BackgroundResult.Loading -> Unit
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReaderLogo()

            UserForm(loading = loading.value, isCreateAccount = !showLoginForm.value) { email, password ->
                if (showLoginForm.value) {
                    viewModel.signInUserAuth(email, password)
                } else {
                    viewModel.createUserAuth(email, password)
                }
            }

            VerticalSpacer(height = 15)

            Row(
                modifier = Modifier.padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val text = if (showLoginForm.value) "Sign up" else "Login"

                Text(text = "New User?")

                Text(
                    text = text,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable(enabled = !loading.value) {
                            showLoginForm.value = !showLoginForm.value
                        },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
