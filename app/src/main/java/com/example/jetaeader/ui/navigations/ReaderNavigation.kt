package com.example.jetaeader.ui.navigations

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetaeader.ui.navigations.ReaderScreens.Companion.getRoute
import com.example.jetaeader.ui.screen.detail.ReaderDetailScreen
import com.example.jetaeader.ui.screen.detail.ReaderDetailViewModel
import com.example.jetaeader.ui.screen.home.ReaderHomeScreen
import com.example.jetaeader.ui.screen.home.ReaderHomeScreenViewModel
import com.example.jetaeader.ui.screen.login.ReaderLoginScreen
import com.example.jetaeader.ui.screen.login.ReaderLoginScreenViewModel
import com.example.jetaeader.ui.screen.search.ReaderSearchScreen
import com.example.jetaeader.ui.screen.search.ReaderSearchScreenViewModel
import com.example.jetaeader.ui.screen.splash.ReaderSplashScreen
import com.example.jetaeader.ui.screen.stats.ReaderStatsScreen
import com.example.jetaeader.ui.screen.stats.ReaderStatsScreenViewModel
import com.example.jetaeader.ui.screen.update.ReaderUpdateScreen
import com.example.jetaeader.ui.screen.update.ReaderUpdateScreenViewModel

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ReaderScreens.SplashScreen.getRoute()
    ) {
        composable(route = ReaderScreens.SplashScreen.getRoute()) {
            ReaderSplashScreen(navController)
        }

        composable(route = ReaderScreens.LoginScreen.getRoute()) {
            val viewModel: ReaderLoginScreenViewModel = hiltViewModel()
            ReaderLoginScreen(navController, viewModel)
        }

        composable(route = ReaderScreens.HomeScreen.getRoute()) {
            val viewModel: ReaderHomeScreenViewModel = hiltViewModel()
            ReaderHomeScreen(navController,  viewModel)
        }

        composable(route = ReaderScreens.SearchScreen.getRoute()) {
            val viewModel: ReaderSearchScreenViewModel = hiltViewModel()
            ReaderSearchScreen(navController, viewModel)
        }

        val notAvailable = "N/A"

        composable(
            route = ReaderScreens.DetailScreen.getRoute(),
            arguments = listOf(
                navArgument(ReaderScreens.BOOK_ID) {
                    type = NavType.StringType
                    defaultValue = notAvailable
                }
            )
        ) {
            val bookId = it.arguments?.getString(ReaderScreens.BOOK_ID) ?: notAvailable
            val viewModel: ReaderDetailViewModel = hiltViewModel()
            ReaderDetailScreen(bookId, navController, viewModel)
        }

        composable(
            route = ReaderScreens.UpdateScreen.getRoute(),
            arguments = listOf(
                navArgument(ReaderScreens.BOOK_ID) {
                    type = NavType.StringType
                    defaultValue = notAvailable
                }
            )
        ) {
            val bookId = it.arguments?.getString(ReaderScreens.BOOK_ID) ?: notAvailable
            val viewModel: ReaderUpdateScreenViewModel = hiltViewModel()
            ReaderUpdateScreen(navController, bookId, viewModel)
        }

        composable(
            route = ReaderScreens.StatsScreen.getRoute(),
            arguments =  listOf(
                navArgument(ReaderScreens.ADDED_BOOK) {
                    type = NavType.IntType
                    defaultValue = 0
                },

                navArgument(ReaderScreens.READING_BOOK) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            val addedBookCount = it.arguments?.getInt(ReaderScreens.ADDED_BOOK, 0) ?: 0
            val readingBookCount = it.arguments?.getInt(ReaderScreens.READING_BOOK, 0) ?: 0
            val viewModel: ReaderStatsScreenViewModel = hiltViewModel()
            ReaderStatsScreen(addedBookCount, readingBookCount, navController, viewModel)
        }
    }
}