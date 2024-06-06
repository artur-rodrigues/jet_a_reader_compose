package com.example.jetaeader.data.api.call

import com.example.jetaeader.data.model.BackgroundResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ReaderResponseAdapter<T>(
    private val successType: Type,
) : CallAdapter<T, Call<BackgroundResult<T>>> {

    override fun responseType() = successType

    override fun adapt(call: Call<T>): Call<BackgroundResult<T>> {
        return  ReaderCall(call)
    }
}