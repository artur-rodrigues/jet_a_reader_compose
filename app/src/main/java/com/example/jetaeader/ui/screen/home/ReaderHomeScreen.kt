package com.example.jetaeader.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetaeader.data.api.firebase.FireBaseUtils
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.ui.components.ErrorMessage
import com.example.jetaeader.ui.components.FABContent
import com.example.jetaeader.ui.components.HorizontalScrollableComponent
import com.example.jetaeader.ui.components.LoadingProgress
import com.example.jetaeader.ui.components.ReaderAppBar
import com.example.jetaeader.ui.components.TitleSection
import com.example.jetaeader.ui.components.VerticalSpacer
import com.example.jetaeader.ui.navigations.ReaderScreens
import com.example.jetaeader.ui.navigations.ReaderScreens.Companion.getRoute
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderHomeScreen(
    navController: NavController,
    viewModel: ReaderHomeScreenViewModel
) {

    val result = remember {
        viewModel.data
    }

    LaunchedEffect(Unit) {
        delay(50)
        viewModel.getAllBooksFromFireBase()
    }

    Scaffold(
        modifier= Modifier.fillMaxWidth(),
        topBar = {
            ReaderAppBar(title = "A. Reader", navController = navController)
        },
        floatingActionButton = {
            FABContent {
                navController.navigate(ReaderScreens.SearchScreen.getRoute())
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            result.value.run {
                when(this) {
                    is BackgroundResult.Error -> ErrorMessage(error?.message ?: exception.message)
                    BackgroundResult.Idle -> {}
                    BackgroundResult.Loading -> LoadingProgress()
                    is BackgroundResult.Success -> HomeContent(data.filterNotNull(), navController)
                }
            }
        }
    }
}

@Composable
fun HomeContent(books: List<MBook>, navController: NavController) {
    val userName = FireBaseUtils.getUserName()
    Column(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxSize()
    ) {

        val addedBooks = books.filter {
            it.startedReading == null && it.finishedReading == null
        }

        val readingNow  = books.filter {
            it.startedReading != null && it.finishedReading == null
        }

        DisplayBookColumns {
            VerticalSpacer(height = 20)
            TitleArea(addedBooks.size, readingNow.size, navController, userName)
            VerticalSpacer(height = it)
            if(readingNow.isNotEmpty()) {
                ReadingRightNowArea(books = readingNow, navController = navController)
            } else {
                EmptyBookList()
            }

        }

        DisplayBookColumns {
            TitleSection(label = "Reading list")
            VerticalSpacer(height = it)
            if(addedBooks.isNotEmpty()) {
                BookListArea(books = addedBooks, navController = navController)
            } else {
                EmptyBookList()
            }
        }
    }
}

@Composable
fun ColumnScope.DisplayBookColumns(compose: @Composable (Int) -> Unit) {
    Column(
        modifier = Modifier.weight(weight = 1f),
        verticalArrangement = Arrangement.Top
    ) {
        compose(20)
    }
}

@Composable
fun EmptyBookList() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(242.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No books added",
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Light,
            fontSize = 18.sp,
            color = Color.LightGray
        )
    }
}

@Composable
fun TitleArea(
    addedBooksCount: Int,
    readingBooksCount: Int,
    navController: NavController,
    userName: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TitleSection(label = "Your reading\n activity right now...")

        Column(
            modifier = Modifier
                .size(50.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = Color.LightGray)
                ) {
                    navController.navigate(
                        ReaderScreens.StatsScreen.name +
                                ReaderScreens.DELIMITER + addedBooksCount +
                                ReaderScreens.DELIMITER + readingBooksCount
                    )

                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = userName,
                modifier = Modifier.padding(2.dp),
                style = MaterialTheme.typography.titleSmall,
                color = Color.Red,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Divider()
        }
    }
}

@Composable
fun ReadingRightNowArea(
    books: List<MBook>,
    navController: NavController
) {
    HorizontalScrollableComponent(books) {
        navController.navigate(ReaderScreens.UpdateScreen.getRoute(value = it))
    }
}

@Composable
fun BookListArea(books: List<MBook>, navController: NavController) {
    HorizontalScrollableComponent(books) {
        navController.navigate(ReaderScreens.UpdateScreen.getRoute(value = it))
    }
}
