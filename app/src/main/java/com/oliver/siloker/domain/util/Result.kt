package com.oliver.siloker.domain.util

import com.oliver.siloker.domain.error.BaseError

sealed interface Result<out D, out E: BaseError> {
    data class Success<out D, out E: BaseError>(val data: D): Result<D, E>
    data class Error<out D, out E: BaseError>(val error: E, val data: D? = null): Result<D, E>
}

inline fun <D, E: BaseError, R> Result<D, E>.map(map: (D) -> R): Result<R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

fun <T, E: BaseError> Result<T, E>.asEmptyDataResult(): Result<Unit, E> {
    return map {  }
}

inline fun <T, E: BaseError> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}


inline fun <T, E: BaseError> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

inline fun <T, E: BaseError> Result<T, E>.onErrorWithData(action: (E, T?) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> {
            action(error, data)
            this
        }
        is Result.Success -> this
    }
}