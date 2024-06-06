package com.example.jetaeader.data.model

sealed class BackgroundResult<out T> {
    class Success<T>(val data: T) : BackgroundResult<T>()
    data object Loading : BackgroundResult<Nothing>()
    data object Idle : BackgroundResult<Nothing>()
    class Error(val exception: Exception, val error: ApiErrorResponse? = null) : BackgroundResult<Nothing>()
}