package com.example.jetaeader.util

import androidx.navigation.NavController
import com.example.jetaeader.ui.navigations.ReaderScreens
import com.example.jetaeader.ui.navigations.ReaderScreens.Companion.getRoute

fun NavController.navigateRemoveSource(from: ReaderScreens, to: ReaderScreens) {
    this.navigate(to.getRoute()) {
        popUpTo(from.getRoute()) {
            inclusive = true
        }
    }
}
