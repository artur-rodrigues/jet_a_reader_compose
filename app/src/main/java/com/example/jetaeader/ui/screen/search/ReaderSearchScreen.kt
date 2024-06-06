package com.example.jetaeader.ui.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.ui.components.ErrorMessage
import com.example.jetaeader.ui.components.InputField
import com.example.jetaeader.ui.components.LoadingProgress
import com.example.jetaeader.ui.components.ReaderAppBar
import com.example.jetaeader.ui.navigations.ReaderScreens
import com.example.jetaeader.ui.navigations.ReaderScreens.Companion.getRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSearchScreen(
    navController: NavController,
    viewModel: ReaderSearchScreenViewModel
) {
    val result = remember {
        viewModel.resultState
    }

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Search books",
                icon = Icons.Default.ArrowBack,
                navController = navController,
                showProfile = false
            ) {
                navController.popBackStack()
            }
        },
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column {
                SearchForm(
                    modifier = Modifier.fillMaxWidth(),
                    loading = result.value == BackgroundResult.Loading
                ) { query ->
                    viewModel.searchBook(query)
                }

                result.value.run {
                    when(this) {
                        is BackgroundResult.Error -> ErrorMessage(error?.message ?: exception.message)
                        BackgroundResult.Idle -> {}
                        BackgroundResult.Loading -> LoadingProgress()
                        is BackgroundResult.Success -> BooksList(books = data) { id ->
                            navController.navigate(ReaderScreens.DetailScreen.getRoute(id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    onSearch: (String) -> Unit = {}
) {
    Column(modifier) {
        val keyBoard = LocalSoftwareKeyboardController.current

        val searchQuery = rememberSaveable {
            mutableStateOf("")
        }

        val valid = searchQuery.value.run {
            remember(this) {
                trim().isNotEmpty()
            }
        }

        InputField(
            valueState = searchQuery,
            labelId = "Search",
            enabled = !loading,
            onAction = KeyboardActions {
                if (!valid) {
                    return@KeyboardActions
                }

                onSearch(searchQuery.value.trim())
                searchQuery.value = ""
                keyBoard?.hide()
            }
        )
    }
}

@Composable
fun BooksList(books: List<MBook>, onBookClick: (String) -> Unit) {
    LazyColumn(
        modifier =Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(books) {
            BookRow(it, onBookClick)
        }
    }
}

@Composable
fun BookRow(book: MBook, onBookClick: (String) -> Unit = {}) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(shape = RoundedCornerShape(20.dp))
                .clickable {
                    onBookClick(book.googleBookId.toString())
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            AsyncImage(
                model = book.photoUrl,
                contentDescription = "Book image",
                modifier = Modifier
                    .height(100.dp)
                    .width(80.dp)
                    .padding(end = 8.dp)
            )

            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = book.title.toString(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black
                    )

                    if (book.rating!! >= 4) {
                        Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "Icon Thumbs UP", tint = Color.Green.copy(alpha = 0.3f))
                    }
                }

                DefaultBookItemText("Author: ${book.authors.toString()}")
                DefaultBookItemText("Date: ${book.publishedDate}")
                DefaultBookItemText("Categories: ${book.categories}")
            }
        }
    }
}

@Composable
fun DefaultBookItemText(text: String) {
    Text(
        modifier = Modifier.padding(end = 8.dp),
        text = text,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 2,
        color = Color.Black,
        fontStyle = FontStyle.Italic
    )
}