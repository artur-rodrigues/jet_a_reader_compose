package com.example.jetaeader.domain.model.converters

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.data.model.BookResponse
import com.example.jetaeader.data.model.Item
import com.example.jetaeader.domain.model.MBook

fun BackgroundResult.Success<BookResponse>.convertToSuccessMBookList(): BackgroundResult<List<MBook>> {
    return BackgroundResult.Success(data.convertToMBookList())
}

fun BookResponse.convertToMBookList(): List<MBook> {
    val mBookList = arrayListOf<MBook>()

    items.forEach {
        mBookList.add(it.convertToMBook())
    }

    return mBookList
}

fun BackgroundResult.Success<Item>.convertToSuccessMBook(): BackgroundResult<MBook> {
    return BackgroundResult.Success(data.convertToMBook())
}

fun Item.convertToMBook(): MBook {
    val separator = ", "

    return volumeInfo.run {
        val authors = authors?.joinToString(separator = separator)
        val categories = categories?.joinToString(separator = separator) ?: "N/A"

        MBook(
            googleBookId = id,
            title = title,
            authors = authors,
            categories = categories,
            publishedDate = publishedDate,
            pageCount = pageCount,
            description = description,
            photoUrl = imageLinks?.thumbnail,
            notes = "",
            rating = 0.0,
            userId = ""
        )
    }
}