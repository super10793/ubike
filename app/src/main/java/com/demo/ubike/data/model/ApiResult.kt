package com.demo.ubike.data.model

sealed class ApiResult {
    data class Success<T>(val response: T) : ApiResult()
    data class Fail(val throwable: Throwable) : ApiResult()
}
