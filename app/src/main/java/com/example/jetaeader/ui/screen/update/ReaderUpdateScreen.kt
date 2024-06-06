package com.example.jetaeader.ui.screen.update

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import com.example.jetaeader.ui.components.InputField
import com.example.jetaeader.ui.components.LoadingProgress
import com.example.jetaeader.ui.components.RatingBar
import com.example.jetaeader.ui.components.ReaderAppBar
import com.example.jetaeader.ui.components.VerticalSpacer
import com.example.jetaeader.util.format
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderUpdateScreen(
    navController: NavController,
    bookId: String,
    viewModel: ReaderUpdateScreenViewModel
) {
    val result = remember {
        viewModel.result
    }

    LaunchedEffect(Unit) {
        delay(50)
        viewModel.getBookFromFireBase(bookId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ReaderAppBar(
                title = "Update book",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                showProfile = false
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
            result.value.run {
                when(this) {
                    is BackgroundResult.Error -> ErrorMessage(error?.message ?: exception.message)
                    BackgroundResult.Idle -> Unit
                    BackgroundResult.Loading -> LoadingProgress()
                    is BackgroundResult.Success -> {
                        if (data != null) {
                            MainContent(book = data,  viewModel = viewModel, navController = navController)
                        } else {
                            ErrorMessage("Book Unavailable")
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun MainContent(book: MBook, viewModel: ReaderUpdateScreenViewModel, navController: NavController) {
    val notesText = remember {
        val notes = if (book.notes?.isNotBlank() == true) {
            book.notes!!
        } else {
            ""
        }
        mutableStateOf(notes)
    }

    val ratingVal = remember {
        mutableIntStateOf( book.rating?.toInt() ?: 0)
    }

    val isStartedReading = remember {
        mutableStateOf(false)
    }

    val isFinishReading = remember {
        mutableStateOf(false)
    }

    val bookUpdate = remember(notesText.value, ratingVal.intValue, isStartedReading.value, isFinishReading.value) {
        val changeNotes = book.notes != notesText.value
        val changeRating = book.rating?.toInt() != ratingVal.intValue
        mutableStateOf(changeNotes || changeRating || isStartedReading.value || isFinishReading.value)
    }

    val resultUpdate = remember {
        viewModel.resultUpdate
    }

    val loadingUpdateState = remember(resultUpdate.value) {
        mutableStateOf(resultUpdate.value == BackgroundResult.Loading)
    }

    val resultDelete = remember {
        viewModel.resultDelete
    }

    val loadingDeleteState = remember(resultDelete.value) {
        mutableStateOf(resultDelete.value == BackgroundResult.Loading)
    }

    val enabledFields = remember(loadingUpdateState.value, loadingDeleteState.value) {
        mutableStateOf(!(loadingUpdateState.value || loadingDeleteState.value))
    }

//    val context = LocalContext.current

    /*val resultAction: @Composable (BackgroundResult<Unit>, String) -> Unit = { result, text ->
        when(result) {
            is BackgroundResult.Success -> LaunchedEffect(Unit) {
                delay(200)
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            is BackgroundResult.Error -> Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }*/

    ResultAction(resultUpdate.value, "Book Update Successfully!", navController)
    ResultAction(resultDelete.value, "Book Delete Successfully!", navController)

    /*resultUpdate.value.run {
        when(this) {
            is BackgroundResult.Success -> LaunchedEffect(Unit) {
                delay(200)
                Toast.makeText(context, "Book Update Successfully!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            is BackgroundResult.Error -> Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }*/

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(book = book)

        SimpleForm(valueState = notesText, loading = ! enabledFields.value)

        StartReadRow(
            isStartedReading = isStartedReading,
            isFinishReading = isFinishReading,
            book = book
        )

        RatingArea(book = book) {
            ratingVal.intValue = it
        }

        VerticalSpacer(height = 15)

        ButtonArea(
            updateBook = bookUpdate.value,
            book = book,
            loadingUpdateState = loadingUpdateState,
            loadingDeleteState = loadingDeleteState,
            viewModel = viewModel
        ) {
            val isStartedTimeStamp = if (isStartedReading.value) {
                Timestamp.now()
            } else {
                book.startedReading
            }
            val isFinishedTimeStamp = if (isFinishReading.value) {
                Timestamp.now()
            } else {
                book.finishedReading
            }

            book.apply {
                notes = notesText.value
                rating = ratingVal.intValue.toDouble()
                startedReading = isStartedTimeStamp
                finishedReading = isFinishedTimeStamp
            }
        }
    }
}

@Composable
fun ResultAction(result: BackgroundResult<Unit>, text: String, navController: NavController) {
    val context = LocalContext.current
    when(result) {
        is BackgroundResult.Success -> LaunchedEffect(Unit) {
            delay(200)
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
        is BackgroundResult.Error -> Toast.makeText(context, result.exception.message, Toast.LENGTH_LONG).show()
        else -> Unit
    }
}

@Composable
fun Header(book: MBook) {
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            HorizontalSpacer(width = 43)

            CardBook(book)

            Column(
                modifier = Modifier
                    .height(100.dp)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = book.title.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = book.authors.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = book.publishedDate.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
fun CardBook(book: MBook) {
    Card(
        modifier = Modifier
            .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        AsyncImage(
            model = book.photoUrl,
            contentDescription = "Book Image",
            modifier = Modifier
                .height(100.dp)
                .width(120.dp)
                .padding(4.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 120.dp,
                        topEnd = 20.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        )
    }
}

@Composable
fun SimpleForm(
    valueState: MutableState<String>,
    modifier: Modifier = Modifier,
    loading: Boolean = false
) {
    Column(
        modifier = modifier
    ) {
        val keyBoardController = LocalSoftwareKeyboardController.current

        InputField(
            modifier = Modifier
                .height(140.dp)
                .padding(3.dp)
                .background(color = Color.White, shape = CircleShape)
                .padding(horizontal = 20.dp, vertical = 20.dp),
            valueState = valueState,
            labelId = "Enter your thoughts",
            enabled = !loading,
            imeAction = ImeAction.Done,
            onAction = KeyboardActions {
                keyBoardController?.hide()
            }
        )
    }
}

@Composable
fun StartReadRow(
    isStartedReading: MutableState<Boolean>,
    isFinishReading: MutableState<Boolean>,
    book: MBook
) {
    val startReading = book.startedReading != null

    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = if (startReading) Alignment.CenterEnd else Alignment.Center
        ) {
            TextButton(
                enabled = !startReading,
                onClick = {
                    isStartedReading.value = true
                }
            ) {
                if (!startReading) {
                    if (!isStartedReading.value) {
                        Text(text = "Start Reading")
                    } else {
                        Text(
                            text = "Started Reading",
                            modifier = Modifier.alpha(alpha = 0.6f),
                            color = Color.Red.copy(alpha = 0.5f)
                        )
                    }
                } else {
                    Text(text = "Started on: ${book.startedReading?.format()}")
                }
            }
        }

        if (startReading) {
            val finishedReading = book.finishedReading != null
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = if (finishedReading) 0.dp else 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                TextButton(
                    enabled = book.finishedReading == null,
                    onClick = {
                        isFinishReading.value = true
                    }
                ) {
                    if (book.finishedReading == null) {
                        if (!isFinishReading.value) {
                            Text(text = "Mark as Read")
                        } else {
                            Text(text = "Finished Reading!")
                        }
                    } else {
                        Text(text = "Finished on: ${book.finishedReading?.format()}")
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingArea(book: MBook, onRating: (Int) -> Unit) {

    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))

    book.rating?.toInt()?.let {
        RatingBar(rating = it) { rating ->
            onRating(rating)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ButtonArea(
    updateBook: Boolean,
    book: MBook,
    loadingUpdateState: MutableState<Boolean>,
    loadingDeleteState: MutableState<Boolean>,
    viewModel: ReaderUpdateScreenViewModel,
    executeUpdate: () -> MBook,
) {
    Row(modifier = Modifier.padding(top = 5.dp)) {
        AReaderButton(
            text = "Update",
            enabled = !loadingUpdateState.value && !loadingDeleteState.value && updateBook,
            showLoading = loadingUpdateState.value,
        ) {
            viewModel.updateBookInFireBase(executeUpdate())
        }

        HorizontalSpacer(width = 60)

        val openDialog = remember {
            mutableStateOf(false)
        }

        if (openDialog.value) {
            val text = "Are you sure you want to delete this book?\nThis action is not reversible"
            ShowAlertDialog(message = text, openDialog = openDialog) {
                viewModel.deleteBookFromFireBase(book)
            }
        }

        AReaderButton(
            text = "Delete",
            enabled = !loadingUpdateState.value && !loadingDeleteState.value,
            showLoading = loadingDeleteState.value,
        ) {
            openDialog.value = true
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ShowAlertDialog(message: String, openDialog: MutableState<Boolean>, onYesPressed: () -> Unit) {
    if (openDialog.value) {
        AlertDialog(
            title = {
                Text(text = "Delete Book")
            },
            text = {
                Text(text = message)
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialog.value = false
                }) {
                    Text(text = "No")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    onYesPressed()
                }) {
                    Text(text = "Yes")
                }
            },
            onDismissRequest = {}
        )
    }
}