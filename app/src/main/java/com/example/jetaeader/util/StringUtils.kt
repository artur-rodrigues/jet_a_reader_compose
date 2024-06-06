package com.example.jetaeader.util

import android.text.Html
import android.text.TextUtils
import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.text.HtmlCompat
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


fun String.toDoubleQuote() = "\"$this\""

fun String.toSimpleQuote() = "\'$this\'"

fun String.textFieldCursorEndState(): MutableState<TextFieldValue> {
    val value = TextFieldValue(
        text = this,
        selection = TextRange(this.length)
    )

    return mutableStateOf(value)
}

fun String.isValidEmail(): Boolean {
    return if (TextUtils.isEmpty(this)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}

fun String.convertHtmlToText(): String {
    return Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}

fun Timestamp.format(format: String = DEFAULT_DATE_PATTERN): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(toDate())
}

