package com.example.jetaeader.ui.components

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jetaeader.R
import com.example.jetaeader.domain.model.MBook
import com.example.jetaeader.ui.navigations.ReaderScreens
import com.example.jetaeader.ui.theme.FABBackground
import com.example.jetaeader.util.ScreenUtils
import com.google.firebase.auth.FirebaseAuth

@Composable
fun VerticalSpacer(height: Int) {
    Spacer(modifier = Modifier.height(height.dp))
}

@Composable
fun HorizontalSpacer(width: Int) {
    Spacer(modifier = Modifier.width(width.dp))
}

@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
    Text(
        text = "A. Reader",
        modifier = modifier.padding(16.dp),
        style = MaterialTheme.typography.displayMedium,
        color = Color.Red.copy(alpha = 0.5f)
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        enabled = enabled,
        singleLine = isSingleLine,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
            .padding(
                start = 10.dp,
                bottom = 10.dp,
                end = 10.dp
            )
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = onAction,
        shape = RoundedCornerShape(12.dp),
        isError = isError,
        supportingText = supportingText,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
}

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(
                start = 10.dp,
                bottom = 10.dp,
                end = 10.dp,
                top = 5.dp
            )
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if(loading) {
            CircularProgressIndicator(modifier = Modifier.size(25.dp))
        } else {
            Text(
                text = textId,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String
) {
    Surface(modifier = modifier.padding(start = 5.dp)) {
        Column {
            Text(
                text = label,
                fontStyle = FontStyle.Normal,
                fontSize = 19.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ReaderAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onBackArrowCLick: () -> Unit = {}
) {
    TopAppBar(
        modifier = Modifier.shadow(0.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showProfile) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Logo icon",
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(12.dp))
                            .scale(0.9f)
                    )
                }

                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = "Arrow back",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .clickable {
                                onBackArrowCLick()
                            }
                    )
                }

                HorizontalSpacer(width = 50)

                Text(
                    text = title,
                    color = Color.Red.copy(alpha = 0.7f),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }
        },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(ReaderScreens.LoginScreen.name) {
                        popUpTo(ReaderScreens.HomeScreen.name) {
                            inclusive = true
                        }
                    }
                }
            }) {
                if(showProfile) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_logout),
                        contentDescription = "Logout"
                    )
                }
            }
        }
    )
}

@Composable
fun FABContent(onTap: (String) -> Unit) {
    FloatingActionButton(
        shape = CircleShape,
        containerColor = FABBackground,
        onClick = {
            onTap("")
        }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add book",
            tint = Color.White
        )
    }
}

@Composable
fun ListCard(
    book: MBook,
    onPressDetails: (String) -> Unit = {}
) {
    val spacing = 10.dp

    Card(
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clip(shape = RoundedCornerShape(29.dp)),
        shape = RoundedCornerShape(29.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onPressDetails(book.id.toString())
                }
        ) {
            Column(
                modifier = Modifier
                    .width(ScreenUtils.getScreenWidthDP() - (spacing * 2)),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 4.dp, end = 15.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = book.photoUrl,
                        contentDescription = "Book image",
                        modifier = Modifier
                            .height(140.dp)
                            .width(100.dp)
                    )

                    HorizontalSpacer(width = 5)

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.padding(1.dp),
                            imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = "Fav icon"
                        )

                        BookRating(score = book.rating ?: 0.0)
                    }
                }

                Text(
                    text = book.title.toString(),
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = book.authors.toString(),
                    modifier = Modifier.padding(4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                val label =  when {
                    book.startedReading == null && book.finishedReading == null -> "Not read"
                    book.startedReading != null && book.finishedReading == null -> "Reading"
                    book.startedReading != null && book.finishedReading != null -> "Read"
                    else -> "Error"
                }
                RoundedTag(label = label, radius = 80)
            }
        }
    }
}

@Composable
fun RoundedTag(label: String = "Reading", radius: Int = 29, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .clip(
                shape = RoundedCornerShape(
                    bottomEndPercent = radius,
                    topStartPercent = radius
                )
            ),
        color = FABBackground
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .heightIn(40.dp)
                .clickable(onClick = onClick),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 15.sp
                )
            )
        }
    }
}

@Composable
fun BookRating(score: Double = 4.5) {
    Surface(
        modifier = Modifier
            .height(70.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        shadowElevation = 6.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.padding(1.dp),
                imageVector = Icons.Outlined.Star,
                contentDescription = "Fav icon"
            )

            Text(text = score.toString(), style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun HorizontalScrollableComponent(books: List<MBook>, minHeight: Int = 280, onPressDetails: (String) -> Unit) {
    Row(
        modifier = Modifier
            .heightIn(minHeight.dp)
            .horizontalScroll(state = rememberScrollState())
    ) {
        for (book in books) {
            ListCard(book, onPressDetails)
        }
    }
}

@Composable
fun MainContentBox(color: Color = Color.Unspecified, compose: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
            .background(color = color),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        compose()
    }
}

@Composable
fun LoadingProgress(color: Color = Color.Unspecified) {
    MainContentBox (color = color) {
        CircularProgressIndicator(modifier = Modifier.size(50.dp))
    }
}

@Composable
fun ErrorMessage(errorMessage: String? = "Error", color: Color = Color.Unspecified) {
    MainContentBox(color = color) {
        Text(text = "Something wrong happen", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Please, try again later",  style = MaterialTheme.typography.bodyLarge)

        errorMessage?.let {
            Spacer(modifier = Modifier.height(20.dp))

            val annotatedMessage = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                    append("Error message: ")
                }

                withStyle(SpanStyle(fontWeight = FontWeight.Light)) {
                    append(errorMessage)
                }
            }

            Text(text = annotatedMessage, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun AReaderButton(
    text: String = "Test",
    enabled: Boolean = true,
    showLoading: Boolean = false,
    onClick: () -> Unit
) {
    val corner = 18.dp

    Button(
        modifier = Modifier.width(120.dp),
        onClick = onClick,
        shape = RoundedCornerShape(topStart = corner, bottomEnd = corner),
        colors = ButtonDefaults.buttonColors(containerColor = FABBackground),
        enabled = enabled
    ) {
        if(enabled) {
            Text(text = text)
        } else {
            if(showLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text(text = text)
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
    var ratingState by remember {
        mutableIntStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy), label = "Rating Animation"
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_star_24),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }
                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}
