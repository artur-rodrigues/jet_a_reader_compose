package com.example.jetaeader.data.api

import com.example.jetaeader.data.model.BackgroundResult
import com.example.jetaeader.data.model.BookResponse
import com.example.jetaeader.data.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BookApi {

    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query: String) : BackgroundResult<BookResponse>

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId: String) : BackgroundResult<Item>
}