package com.demo.ubike.result

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data

/**
 * Updates value of [liveData] if [Result] is of type [Success]
 */
inline fun <reified T> Result<T>.updateOnSuccess(liveData: MutableLiveData<T>) {
    if (this is Result.Success) {
        liveData.value = data
    }
}

/**
 * Updates value of [MutableStateFlow] if [Result] is of type [Success]
 */
inline fun <reified T> Result<T>.updateOnSuccess(stateFlow: MutableStateFlow<T>) {
    if (this is Result.Success) {
        stateFlow.value = data
    }
}

inline fun <reified T, reified U> Result<T>.map(mapper: (result: T) -> U): Result<U> {
    return when (this) {
        is Result.Success -> Result.Success(mapper(data))
        is Result.Error -> this
    }
}

inline fun <reified T> Result<T>.mapError(mapper: (exception: Exception) -> Exception): Result<T> {
    return when (this) {
        is Result.Success -> this
        is Result.Error -> Result.Error(mapper(exception))
    }
}

inline fun <reified T, reified U> Result<T>.flatMap(mapper: (T) -> Result<U>): Result<U> {
    return when (this) {
        is Result.Success -> mapper(data)
        is Result.Error -> this
    }
}

inline fun <reified T> Result<T>.flatMapError(mapper: (exception: Exception) -> Result.Error): Result<T> {
    return when (this) {
        is Result.Success -> this
        is Result.Error -> mapper(exception)
    }
}