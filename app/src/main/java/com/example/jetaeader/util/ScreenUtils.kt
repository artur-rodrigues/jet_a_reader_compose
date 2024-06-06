package com.example.jetaeader.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

object ScreenUtils {

    @Composable
    fun getScreenWidthPixels(): Int {
        return LocalContext.current.resources.displayMetrics.widthPixels
    }

    @Composable
    fun getScreenHeightPixels(): Int {
        return LocalContext.current.resources.displayMetrics.heightPixels
    }

    @Composable
    fun getScreenWidthDP(): Dp {
        return pixelToDP(getScreenWidthPixels())
    }

    @Composable
    fun getScreenHeightDP(): Dp {
        return pixelToDP(getScreenHeightPixels())
    }

    @Composable
    fun pixelToDP(pixels: Int): Dp {
        return with(LocalDensity.current) {
            pixels.toDp()
        }
    }
}