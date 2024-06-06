package com.example.jetaeader.ui.navigations

enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    HomeScreen,
    SearchScreen,
    DetailScreen,
    UpdateScreen,
    StatsScreen;

    companion object {

        const val DELIMITER = "/"
        const val BOOK_ID = "book_id"
        const val ADDED_BOOK = "added_book"
        const val READING_BOOK = "reading_book"

        fun fromRoute(route: String): ReaderScreens {
            return when(route.substringBefore(DELIMITER)) {
                SplashScreen.name -> SplashScreen
                LoginScreen.name -> LoginScreen
                HomeScreen.name -> HomeScreen
                SearchScreen.name -> SearchScreen
                DetailScreen.name -> DetailScreen
                UpdateScreen.name -> UpdateScreen
                StatsScreen.name -> StatsScreen
                else -> HomeScreen
            }
        }

        fun ReaderScreens.getRoute(value: String? = null): String {
            return when(this) {
                SplashScreen -> SplashScreen.name
                LoginScreen -> LoginScreen.name
                HomeScreen -> HomeScreen.name
                SearchScreen -> SearchScreen.name
                DetailScreen -> DetailScreen.name + DELIMITER + (value ?: addCurlyBraces(BOOK_ID))
                UpdateScreen -> UpdateScreen.name + DELIMITER + (value ?: addCurlyBraces(BOOK_ID))
                StatsScreen -> StatsScreen.name + DELIMITER + addCurlyBraces(ADDED_BOOK) + DELIMITER + addCurlyBraces(READING_BOOK)
            }
        }

        private fun <T> addCurlyBraces(value: T): String {
            return "{${value.toString()}}"
        }
    }
}