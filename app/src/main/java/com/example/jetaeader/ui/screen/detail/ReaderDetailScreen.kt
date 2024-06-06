package com.example.jetaeader.ui.screen.detail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.ui.components.AReaderButton
import com.example.jetaeader.ui.components.ErrorMessage
import com.example.jetaeader.ui.components.HorizontalSpacer
import com.example.jetaeader.ui.components.LoadingProgress
import com.example.jetaeader.ui.components.ReaderAppBar
import com.example.jetaeader.ui.components.VerticalSpacer
import com.example.jetaeader.util.ScreenUtils
import com.example.jetaeader.util.convertHtmlToText
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderDetailScreen(
    bookId: String = "",
    navController: NavController,
    viewModel: ReaderDetailViewModel
) {

    val result = remember {
        viewModel.resultGetState
    }

    LaunchedEffect(Unit) {
        delay(50)
        viewModel.getBook(bookId)
    }

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Detail",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                result.value.run {
                    when(this) {
                        is BackgroundResult.Error -> ErrorMessage(error?.message ?: exception.message)
                        BackgroundResult.Idle -> {}
                        BackgroundResult.Loading -> LoadingProgress()
                        is BackgroundResult.Success ->
                            ShowBookDetails(
                                mBook = data,
                                navController = navController,
                                viewModel = viewModel
                            )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowBookDetails(mBook: MBook, navController: NavController, viewModel: ReaderDetailViewModel) {
    mBook.run {
        Card(
            modifier = Modifier.padding(34.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Book image",
                modifier = Modifier
                    .size(90.dp)
                    .padding(1.dp)
                    .background(color = Color.White)
            )
        }

        Text(
            text = title.toString(),
            style = MaterialTheme.typography.headlineSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            textAlign = TextAlign.Center
        )

        VerticalSpacer(height = 8)

        BookInfo(label = "Authors", info = authors.toString())
        BookInfo(label = "Page Count", info = pageCount.toString())
        BookInfo(label = "Categories", info = categories.toString())
        BookInfo(label = "Published", info = publishedDate.toString())

        VerticalSpacer(height = 8)

        BookDescription(description.toString())

        ButtonArea(book = this, navController = navController, viewModel = viewModel)
    }
}

@Composable
fun BookDescription(description: String) {
    Surface(
        modifier = Modifier
            .height(
                ScreenUtils
                    .getScreenHeightDP()
                    .times(0.35f)
            )
            .padding(4.dp),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.DarkGray)
    ) {
        LazyColumn(modifier = Modifier.padding(5.dp)) {
            item {
                Text(text = description.convertHtmlToText())
            }
        }
    }
}

@Composable
fun BookInfo(label: String, info: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$label: ",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 4.dp),
            fontSize = 18.sp
        )

        Text(
            text = info,
            fontSize = 18.sp,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ButtonArea(book: MBook, navController: NavController, viewModel: ReaderDetailViewModel) {
    val context = LocalContext.current

    val result = remember {
        viewModel.resultCreateState
    }

    val loading = remember(result.value) {
        mutableStateOf(result.value == BackgroundResult.Loading)
    }

    result.value.run {
        when(this) {
            is BackgroundResult.Error -> Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            is BackgroundResult.Success -> LaunchedEffect(Unit) {
                delay(200)
                navController.popBackStack()
            }
            else-> Unit
        }
    }

    Row(modifier = Modifier.padding(top = 5.dp)) {
        AReaderButton(
            text = "Save",
            enabled = !loading.value,
            showLoading = loading.value
        ) {
            viewModel.saveBookFirebase(book)
        }

        HorizontalSpacer(width = 20)

        AReaderButton(text = "Cancel", enabled = !loading.value) {
            navController.popBackStack()
        }
    }
}