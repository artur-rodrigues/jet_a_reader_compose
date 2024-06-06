package com.example.jetaeader.ui.screen.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetaeader.ui.components.ReaderLogo
import com.example.jetaeader.ui.components.VerticalSpacer
import com.example.jetaeader.ui.navigations.ReaderScreens
import com.example.jetaeader.util.navigateRemoveSource
import com.example.jetaeader.util.toDoubleQuote
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun ReaderSplashScreen(navController: NavController) {
    val scale =  remember {
        Animatable(initialValue = 0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }
            )
        )

        delay(200L)

        val route = if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            ReaderScreens.LoginScreen
        } else {
            ReaderScreens.HomeScreen
        }

        navController.navigateRemoveSource(ReaderScreens.SplashScreen, route)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .padding(15.dp)
                .size(330.dp)
                .scale(scale.value),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(width = 2.dp, color = Color.LightGray)
        ) {
            Column(
                modifier = Modifier
                    .padding(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ReaderLogo()

                VerticalSpacer(height = 15)

                Text(
                    text = "Read. Change. Yourself".toDoubleQuote(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.LightGray
                )
            }
        }
    }
}
