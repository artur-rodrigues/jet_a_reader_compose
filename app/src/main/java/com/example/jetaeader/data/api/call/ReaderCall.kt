package com.example.jetaeader.data.api.call

import com.example.jetaeader.data.model.ApiErrorResponse
import com.example.jetaeader.data.model.BackgroundResult
import com.google.gson.Gson
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class ReaderCall<T>(
    private val delegate: Call<T>,
) : Call<BackgroundResult<T>> {

    override fun clone(): Call<BackgroundResult<T>> = ReaderCall(delegate.clone())

    override fun execute(): Response<BackgroundResult<T>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun isExecuted() = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled()  = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = Timeout().timeout(30, TimeUnit.SECONDS)

    override fun enqueue(callback: Callback<BackgroundResult<T>>) {
        return delegate.enqueue(getCallBack(callback))
    }

    private fun getCallBack(callback: Callback<BackgroundResult<T>>) = object : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {

            val result = if(response.isSuccessful) {
                when(val body = response.body()) {
                    null -> BackgroundResult.Error(Exception("Response body is null"))
                    else -> BackgroundResult.Success(body)
                }
            } else {
                try {
                    val error = Gson().fromJson(response.errorBody()?.string(), ApiErrorResponse::class.java)

                    BackgroundResult.Error(Exception("Api Error: ${error.message}"), error)
                } catch (e: Exception) {
                    e.printStackTrace()
                    BackgroundResult.Error(Exception("Error converting Error Body", e))
                }

            }

            sendResponse(result)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            sendResponse(BackgroundResult.Error(Exception(t)))
        }

        private fun sendResponse(result: BackgroundResult<T>) {
            callback.onResponse(this@ReaderCall, Response.success(result))
        }
    }
}