package com.example.jetaeader.ui.screen.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetaeader.data.api.firebase.FireBaseUtils
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.ui.components.ErrorMessage
import com.example.jetaeader.ui.components.LoadingProgress
import com.example.jetaeader.ui.components.ReaderAppBar
import com.example.jetaeader.ui.screen.search.BookRow
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderStatsScreen(
    addedBooksCount: Int,
    readingBooksCount: Int,
    navController: NavController,
    viewModel: ReaderStatsScreenViewModel
) {

    val result = remember {
        viewModel.data
    }

    LaunchedEffect(Unit) {
        delay(50)
        viewModel.getAllBooksFromFireBase()
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            ReaderAppBar(
                title = "Book Stats",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            result.value.run {
                when(this) {
                    is BackgroundResult.Error -> ErrorMessage(error?.message ?: exception.message)
                    BackgroundResult.Idle -> {}
                    BackgroundResult.Loading -> LoadingProgress()
                    is BackgroundResult.Success -> {
                        val readBooks = data.filterNotNull().filter { book ->
                            book.startedReading != null && book.finishedReading != null
                        }

                        MainContent(addedBooksCount, readingBooksCount, readBooks)
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(
    addedBooksCount: Int,
    readingBooksCount: Int,
    books: List<MBook>
) {
    Column {
        UserRow()

        CardStatus(addedBooksCount = addedBooksCount, readingBooksCount = readingBooksCount)

        Divider()

        ListOfBooks(books = books)
    }
}

@Composable
fun UserRow() {
    val userName = FireBaseUtils.getUserName()
    Row(
        modifier = Modifier.padding(start = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Sharp.Person, contentDescription = "Icon person")
        }

        Text(text = "Hi, ${userName.uppercase(Locale.getDefault())} ")
    }
}

@Composable
fun CardStatus(
    addedBooksCount: Int,
    readingBooksCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 30.dp, top = 8.dp, bottom = 8.dp)
                .background(color = Color.Transparent),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Your stats", style = MaterialTheme.typography.headlineSmall)
            Divider()
            Text(text = "You'll read: $addedBooksCount")
            Text(text = "You're reading: $readingBooksCount")
        }
    }
}

@Composable
fun ListOfBooks(books: List<MBook>) {
    if (books.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(books) {book ->
                BookRow(book = book)
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Books Available",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.LightGray
            )
        }
    }
}